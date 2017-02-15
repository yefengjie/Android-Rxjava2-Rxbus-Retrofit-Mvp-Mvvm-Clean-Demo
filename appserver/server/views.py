# coding=utf-8
# Create your views here.
from __future__ import print_function

import json
import time

from django.core.exceptions import ObjectDoesNotExist
from django.views.decorators.csrf import csrf_exempt

from server.models import Book
from util.dictutil import to_dict
from util.httputil import json_ok, json_error


def getSampleBooks(request):
    # 模拟网络延迟
    time.sleep(5)
    print(request.GET.get('appVersion'))
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


@csrf_exempt
def saveSampleBook(request):
    bookJson = json.loads(request.body)
    b = Book(id=bookJson['id'], title=bookJson['title'])
    b.save()
    print(str(b.id))
    return json_ok('ok')


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
        return json_error('delete failed. can not find this book')
    return json_ok('delete success')
