package com.ymx.driver.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

public abstract class BaseListViewModel<D> extends BaseViewModel {

    protected PagedList.Config config;
    private DataSource dataSource;
    private LiveData<PagedList<D>> pageData;

    private MutableLiveData<Boolean> boundaryPageData = new MutableLiveData<>();

    public BaseListViewModel(@NonNull Application application) {
        super(application);

        config = new PagedList.Config.Builder()
                .setPageSize(10)
                .setInitialLoadSizeHint(12)
                // .setMaxSize(100)；
                // .setEnablePlaceholders(false)
                // .setPrefetchDistance()
                .build();

        pageData = new LivePagedListBuilder(factory, config)
                .setInitialLoadKey(0)
                //.setFetchExecutor()
                .setBoundaryCallback(callback)
                .build();
    }


    PagedList.BoundaryCallback<D> callback = new PagedList.BoundaryCallback<D>() {
        @Override
        public void onZeroItemsLoaded() {
            //新提交的PagedList中没有数据
            boundaryPageData.postValue(false);
        }

        @Override
        public void onItemAtFrontLoaded(@NonNull D itemAtFront) {
            //新提交的PagedList中第一条数据被加载到列表上
            boundaryPageData.postValue(true);
        }

        @Override
        public void onItemAtEndLoaded(@NonNull D itemAtEnd) {
            //新提交的PagedList中最后一条数据被加载到列表上
        }
    };

    DataSource.Factory factory = new DataSource.Factory() {
        @NonNull
        @Override
        public DataSource create() {
            if (dataSource == null || dataSource.isInvalid()) {
                dataSource = createDataSource();
            }
            return dataSource;
        }
    };

    public abstract DataSource createDataSource();


    //可以在这个方法里 做一些清理 的工作
    @Override
    protected void onCleared() {

    }
}
