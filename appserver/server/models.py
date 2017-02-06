# coding=utf-8
from __future__ import unicode_literals

from django.db import models


# Create your models here.
class Book(models.Model):
    title = models.CharField('标题', max_length=100, blank=True, null=True, default='')
    pic = models.URLField('封面')

    def __unicode__(self):
        return self.title
