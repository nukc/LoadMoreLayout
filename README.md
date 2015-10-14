# LoadMoreLayout

  这是一个上拉加载更多控件，看过用过不少刷新控件，但很多都是只支持下拉刷新，好吧，那就自己做一个吧，支持API 11及以上。

# 用法

1. 导入库

    ``` compile 'com.github.nukc.loadmorelayout:library:0.1' ```

2. 在你的 layout 里添加 LoadMoreLayout 控件

 	```xml
     <com.nukc.library.LoadMoreLayout
         android:id="@+id/loadMoreLayout"
         android:layout_width="match_parent"
         android:layout_height="match_parent">

         <android.support.v7.widget.RecyclerView
             android:id="@+id/recyclerView"
             android:layout_width="match_parent"
             android:layout_height="match_parent"
             android:background="@android:color/white"/>
     </com.nukc.library.LoadMoreLayout>
     ```
3. 引用控件和设置setOnLoadMoreListener

    ```java
        mLoadMoreLayout = (LoadMoreLayout) findViewById(R.id.loadMoreLayout);
        mLoadMoreLayout.setOnLoadMoreListener(new LoadMoreLayout.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mLoadMoreLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mLoadMoreLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });
    ```

可以使用这个方法设置刷新状态：

    ```java
        mLoadMoreLayout.setRefreshing(boolean isRefreshing);
    ```


# Thanks

* [Yalantis/Phoenix](https://github.com/Yalantis/Phoenix)
* [android-cjj/BeautifulRefreshLayout](https://github.com/android-cjj/BeautifulRefreshLayout)

## License

    Copyright 2015, nukc

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
