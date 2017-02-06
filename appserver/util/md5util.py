def md5(s):
    import hashlib
    import types
    if isinstance(s, types.StringType):
        m = hashlib.md5()
        m.update(s)
        return m.hexdigest()
    else:
        return ''
