package com.moons.xst.track.adapter.searchadapter.factorymethod;

public class QueryDJPCUserFactory implements SearchAdapterFactory  {

	@Override
	public SearchAdapter getAdapter() {
		return new QueryDJPCUserAdapter();
	}

}
