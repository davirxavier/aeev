package com.davixavier.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import com.davixavier.application.dbcache.CacherRunnable;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

public class ExecuterServices
{
	private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
	private static final ScheduledExecutorService CACHE_EXECUTOR = Executors.newSingleThreadScheduledExecutor();

	public static void requestSync()
	{
		CACHE_EXECUTOR.submit(new CacherRunnable());
	}
	
	public static ExecutorService getExecutor()
	{
		return EXECUTOR;
	}
	
	public static ScheduledExecutorService getCacheExecutor()
	{
		return CACHE_EXECUTOR;
	}
}
