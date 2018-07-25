package com.moons.xst.track.adapter.searchadapter.factorymethod;

public class OperateBillFactory implements SearchAdapterFactory {
	
	@Override
	public SearchAdapter getAdapter() {
		return new OperateBillAdapter();
	}
	
}