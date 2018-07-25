package com.moons.xst.track.adapter.searchadapter.factorymethod;

public class QueryDJLineFactory implements SearchAdapterFactory {
	
	@Override
	public SearchAdapter getAdapter() {
		return new QueryDJLineAdapter();
	}
	
}