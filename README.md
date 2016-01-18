# LoadMoreLayout

  这是一个上拉加载更多控件，看过用过不少刷新控件，但很多都是只支持下拉刷新，好吧，那就自己做一个吧，支持API 11及以上。
  
  <img src="https://raw.githubusercontent.com/nukc/LoadMoreLayout/master/screenshot/screenshot.png">
  
  <img src="https://raw.githubusercontent.com/nukc/LoadMoreLayout/master/screenshot/sample1.gif">

## Usage

1. 导入库:

    ``` compile 'com.github.nukc:library:0.4.1' ```

2. 在你的 layout 里添加 LoadMoreLayout 控件:

	```xml
    <com.nukc.loadmorelayout.LoadMoreLayout
             android:id="@+id/loadMoreLayout"
             android:layout_width="match_parent"
             android:layout_height="match_parent">
    
         <android.support.v7.widget.RecyclerView
             android:id="@+id/recyclerView"
             android:layout_width="match_parent"
             android:layout_height="match_parent"
             android:background="@android:color/white"/>
    </com.nukc.loadmorelayout.LoadMoreLayout>
    ```

     
3. 引用控件和设置setOnLoadMoreListener:

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

可以使用这个方法设置是否启用上拉加载更多：

```java
    mLoadMoreLayout.setLoadMoreEnabled(boolean isLoadMoreEnabled);
```

设置可上拉的最大高度

```java
    mLoadMoreLayout.setPullMaxHeight(int maxHeight)
```

设置触发加载的高度

```java
    mLoadMoreLayout.setBoundedLoadMoreHeight(int boundedHeight)
```

##Customization

新增了个纯文本展示的刷新视图（TextRefreshView）,有2种方法进行设置:
```java
    mLoadMoreLayout.setRefreshView(new TextRefreshView(this));
```
或
```xml
    <com.nukc.loadmorelayout.LoadMoreLayout
        ......
        app:style="text"
    />
```

set custom refresh view 
设置自定义刷新视图（一个继承BaseRefreshView的视图），可以参考[RainRefreshView](https://github.com/nukc/LoadMoreLayout/blob/master/library/src/main/java/com/nukc/loadmorelayout/view/RainRefreshView.java)和[TextRefreshView](https://github.com/nukc/LoadMoreLayout/blob/master/library/src/main/java/com/nukc/loadmorelayout/view/TextRefreshView.java)。
```java
    mLoadMoreLayout.setRefreshView(BaseRefreshView refreshView);
```


##Changelog

###Version: 0.4.0
  * 新增刷新视图，增加可定制性

###Veision: 0.3.5
  * 修改FooterView -> OnLoadMore - > ValueAnimator.ofInt(int... values) 的values值，这里的值直接关联上拉触发后的波动

###Version: 0.3.3
  * 更改RainView的部分逻辑

###Version: 0.3.0
  * 删除library下strings.xml的app_name，因为与app默认创建的app_name重叠

###Version: 0.2.2

  * 增加设置是否启用上拉加载更多的方法
   （当与CoordinatorLayout一起结合使用，app:layout_behavior="@string/appbar_scrolling_view_behavior"，
    且RecyclerView的item太少未占满LoadMoreLayout的时候，CoordinatorLayout的滚动事件无法触发，
    这时可调用mLoadMoreLayout.setLoadMoreEnabled(false);关闭上拉加载。或者不想使用的时候可自行调用此方法。）

## Thanks

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
