package com.davixavier.utils;

import java.util.List;

import org.json.JSONObject;

public interface JSONEncoder<T>
{
	public JSONObject encode(List<T> list);
	public List<T> decode(JSONObject object);
}
