"""JSON helper functions"""
try:
    import simplejson as json
except ImportError:
    import json

from django.http import HttpResponse

POST_ONLY = 'Only POST are allowed'
NET_CODE_OK = 0
NET_CODE_ERROR = 1
NET_CODE_NEED_LOGIN = 10001


def json_res(data, dump=True, status=200):
    return HttpResponse(
        json.dumps(data) if dump else data,
        content_type='application/json',
        status=status,
    )


def json_ok(data):
    return json_res({'data': data, 'code': NET_CODE_OK})


def json_error(error_string, status=200):
    data = {
        'code': NET_CODE_ERROR,
        'msg': error_string,
    }
    return json_res(data)
