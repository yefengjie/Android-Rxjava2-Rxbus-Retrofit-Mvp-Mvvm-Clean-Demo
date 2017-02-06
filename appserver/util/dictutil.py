from django.db.models import DateTimeField
from django.db.models.fields.related import ManyToManyField


def to_dict(instance):
    if instance is None:
        return {}
    opts = instance._meta
    data = {}
    for f in opts.concrete_fields + opts.many_to_many:
        if isinstance(f, ManyToManyField):
            if instance.pk is None:
                data[f.name] = []
            else:
                data[f.name] = list(f.value_from_object(instance).values_list('pk', flat=True))

        elif isinstance(f, DateTimeField):
            data[f.name] = f.value_from_object(instance).strftime("%Y-%m-%d")
        else:
            data[f.name] = f.value_from_object(instance)
    return data
