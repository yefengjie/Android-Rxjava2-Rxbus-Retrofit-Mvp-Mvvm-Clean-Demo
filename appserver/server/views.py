# coding=utf-8
# Create your views here.
from django.core.exceptions import ObjectDoesNotExist

from server.models import Book
from util.dictutil import to_dict
from util.httputil import json_ok
import time


def getSampleBooks(request):
    # 模拟网络延迟
    time.sleep(5)
    print request.GET.get('appVersion')
    books = Book.objects.all()
    book_list = []
    for book in books:
        book_list.append(to_dict(book))
    return json_ok(book_list)


def getSampleBook(request):
    id = request.GET.get('id')
    try:
        book = Book.objects.get(id=id)
    except ObjectDoesNotExist:
        book = None
    return json_ok(to_dict(book))


def saveSampleBook(request):
    title = request.GET.get('title')
    pic = request.GET.get('pic')
    book = Book(title=title, pic=pic)
    book.save()
    return json_ok(to_dict(book))


def deleteSampleBooks(request):
    Book.objects.all().delete()
    return json_ok('delete success')


def deleteSampleBook(request):
    id = request.GET.get('id')
    try:
        book = Book.objects.get(id=id)
        book.delete()
    except ObjectDoesNotExist:
        book = None
    return json_ok('delete success')
