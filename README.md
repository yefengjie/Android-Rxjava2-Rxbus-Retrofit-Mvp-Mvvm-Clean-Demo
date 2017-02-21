# Android-Rxjava2-Rxbus-Retrofit-Mvp-Demo
android mvp architecture demo

### Summary
This is a android mvp sample used RxJava2, based on [google android-architecture project](https://github.com/googlesamples/android-architecture)

### Mvp Structure
buildsystem: debug.jks,config.gradle. add dependencies library in config.gradle

appserver: a simple django server, used for app http request test..

support: app support libraries. such as http lib, rxbus, utils, etc...

base: app basic classes, such as: Application, BaseActivity, BaseFragment, etc..

data: data layer. data model. memory,local,remote data source

ui: presentation layer

### Clean Architecture Structure

domain: Holds all business logic. The domain layer starts with classes named use cases or interactors used by the application presenters. These use cases represent all the possible actions a developer can perform from the presentation layer.

### Mvvm

databinding: uses the Data Binding library to display data and bind UI elements to actions.

### Libraries
retrofit,okhttp,retrofit2_rxjava2_adapter

rxandroid,rxjava

greendao

yf_list_recycler_view

glide

timber

### How to run server
first: make sure you have install django

second: cd appserver category.then run this commend below

```Cmd
python manage.py runserver 0.0.0.0:8000
```

### How to set app
see [AppInit](https://github.com/yefengfreedom/Android-Rxjava2-Rxbus-Retrofit-Mvp-Demo/blob/master/app/src/main/java/com/yefeng/androidarchitecturedemo/base/AppInit.java)
```Java
        // init app log
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        // init app info
        AppInfo.init(app);

        // set http
        Http.setTimeOut(30);
        Http.setHost(LOCAL_HOST, Http.DEFAULT_API_VERSION);

        // init http common params
        new HttpCommonParams.Builder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept-Charset", "utf-8")
                .addHeader("Accept-Encoding", "gzip, deflate")
                .addParam("appVersion", AppInfo.appVersion)
                .addParam("appName", AppInfo.appName);

        // init green db
        DbGreen.getInstance().init(app);
```

### How to use retrofit http
see
[BookApi](https://github.com/yefengfreedom/Android-Rxjava2-Rxbus-Retrofit-Mvp-Demo/blob/master/app/src/main/java/com/yefeng/androidarchitecturedemo/data/source/book/remote/BookApi.java)
[BookRemoteDataSource](https://github.com/yefengfreedom/Android-Rxjava2-Rxbus-Retrofit-Mvp-Demo/blob/master/app/src/main/java/com/yefeng/androidarchitecturedemo/data/source/book/remote/BookRemoteDataSource.java)
```Java
    public interface BookApi {
        @GET("getBooks")
        Flowable<HttpRes<List<Book>>> getBooks();

        @POST("saveBook")
        Flowable<HttpRes<String>> saveBook(@Body @NonNull Book book);

        @GET("deleteBook")
        Flowable<HttpRes<String>> deleteBook(@Query("id") @NonNull String id);
    }

    public BookApi getApi() {
        return HttpRetrofit.getInstance().getService(BookApi.class);
    }

    @Override
    public Flowable<List<Book>> getBooks() {
        return getApi().getBooks().map(new HttpResFunction<>());
    }
```

### How to use RxBus
see
[MainPresenter](https://github.com/yefengfreedom/Android-Rxjava2-Rxbus-Retrofit-Mvp-Demo/blob/master/app/src/main/java/com/yefeng/androidarchitecturedemo/ui/main/MainPresenter.java)
[Events](https://github.com/yefengfreedom/Android-Rxjava2-Rxbus-Retrofit-Mvp-Demo/blob/master/app/src/main/java/com/yefeng/androidarchitecturedemo/ui/main/Events.java)
[MainActivity](https://github.com/yefengfreedom/Android-Rxjava2-Rxbus-Retrofit-Mvp-Demo/blob/master/app/src/main/java/com/yefeng/androidarchitecturedemo/ui/main/MainActivity.java)
```Java
    public static class ReloadEvent {
        boolean mForceUpdate;

        public ReloadEvent(boolean forceUpdate) {
            mForceUpdate = forceUpdate;
        }
    }

    private CompositeDisposable mCompositeDisposable;

    @Override
    public void subscribe() {
        initRxBus();
    }

    private void initRxBus() {
        mCompositeDisposable.add(RxBus.getBus()
                .toObserverable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof Events.ReloadEvent) {
                        loadBooks(((Events.ReloadEvent) o).mForceUpdate);
                    }
                }));
    }

    @Override
    public void unSubscribe() {
        mCompositeDisposable.clear();
    }


    RxBus.getBus().send(new Events.ReloadEvent(false));
```

### How to use RxJava
see
[MainPresenter](https://github.com/yefengfreedom/Android-Rxjava2-Rxbus-Retrofit-Mvp-Demo/blob/master/app/src/main/java/com/yefeng/androidarchitecturedemo/ui/main/MainPresenter.java)
```Java
        mCompositeDisposable.add(
                mBookRepository.getBooks(forceUpdate)
                        .compose(new HttpSchedulersTransformer<>())
                        .doOnSubscribe(new Consumer<Subscription>() {
                            @Override
                            public void accept(Subscription subscription) throws Exception {
                                Timber.d("method: %s, thread: %s_%s", "doOnSubscribe()", Thread.currentThread().getName(), Thread.currentThread().getId());
                                Timber.d("doOnSubscribe()");
                                mMainView.onLoading();
                            }
                        })
                        .subscribe(new Consumer<List<Book>>() {
                            @Override
                            public void accept(List<Book> books) throws Exception {
                                Timber.d("onNext()");
                                Timber.e(books.toString());
                                mMainView.onLoadOk(new ArrayList<>(books));
                                Timber.d("method: %s, thread: %s_%s", "onNext()", Thread.currentThread().getName(), Thread.currentThread().getId());
                            }
                        }, throwable -> {
                            Timber.d("method: %s, thread: %s_%s", "onError()", Thread.currentThread().getName(), Thread.currentThread().getId());
                            Timber.e(throwable);
                            mMainView.onLoadError(throwable.getMessage());
                        }, new Action() {
                            @Override
                            public void run() throws Exception {
                                Timber.d("method: %s, thread: %s_%s", "onComplete()", Thread.currentThread().getName(), Thread.currentThread().getId());
                                Timber.d("onComplete()");
                                mMainView.onLoadFinish();
                            }
                        }));
```


### preview
![alt tag](https://github.com/yefengfreedom/Android-Rxjava2-Rxbus-Retrofit-Mvp-Demo/blob/master/preview/3.png)
![alt tag](https://github.com/yefengfreedom/Android-Rxjava2-Rxbus-Retrofit-Mvp-Demo/blob/master/preview/1.png)
![alt tag](https://github.com/yefengfreedom/Android-Rxjava2-Rxbus-Retrofit-Mvp-Demo/blob/master/preview/2.png)
