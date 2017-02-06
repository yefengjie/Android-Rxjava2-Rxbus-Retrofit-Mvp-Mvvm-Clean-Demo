# coding=utf-8
import uuid

from md5util import md5


def create_token():
    return md5(str(uuid.uuid1()))
