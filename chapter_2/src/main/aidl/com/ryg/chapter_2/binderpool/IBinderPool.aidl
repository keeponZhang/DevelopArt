package com.ryg.chapter_2.binderpool;

interface IBinderPool {

    /**
     * @param binderCode, the unique token of specific Binder<br/>
     * @return specific Binder who's token is binderCode.
     * 这个比较重要，BinderPoolService onBinder返回的就是该存根的实例
     */
    IBinder queryBinder(int binderCode);
}