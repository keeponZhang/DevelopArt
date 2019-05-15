package com.ryg.chapter_2.aidl;

import android.os.RemoteException;

import java.util.ArrayList;
import java.util.List;

/**
 * @创建者 keepon
 * @创建时间 2019/2/27 0027 下午 11:22
 * @描述 ${TODO}
 * @版本 $$Rev$$
 * @更新者 $$Author$$
 * @更新时间 $$Date$$
 */
public class IBookManagerStubImpl extends IBookManager.Stub {
	@Override
	public List<Book> getBookList() throws RemoteException {
		return new ArrayList<>();
	}

	@Override
	public void addBook(Book book) throws RemoteException {

	}

	@Override
	public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {

	}

	@Override
	public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {

	}
}
