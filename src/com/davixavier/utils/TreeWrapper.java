package com.davixavier.utils;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

public class TreeWrapper<T> extends RecursiveTreeObject<TreeWrapper<T>>
{
	private Object userData;
	private T value;

	public T getValue()
	{
		return value;
	}

	public void setValue(T value)
	{
		this.value = value;
	}

	public Object getUserData()
	{
		return userData;
	}

	public void setUserData(Object userData)
	{
		this.userData = userData;
	}
}
