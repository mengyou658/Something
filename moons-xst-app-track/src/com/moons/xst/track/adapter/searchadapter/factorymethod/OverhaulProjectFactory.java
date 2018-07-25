package com.moons.xst.track.adapter.searchadapter.factorymethod;

public class OverhaulProjectFactory implements SearchAdapterFactory {
	
	@Override
	public SearchAdapter getAdapter() {
		return new OverhaulProject_Adapter();
	}
	
}