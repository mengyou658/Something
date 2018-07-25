package com.moons.xst.track.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.moons.xst.track.common.StringUtils;
import com.moons.xst.buss.OverhaulHelper;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.OverhaulProject;

public class OverhaulProjectAdapter extends BaseAdapter {

	private Context context;
	private List<OverhaulProject> listItems;
	private LayoutInflater listContainer;
	private int itemViewResource;
	private ListView listView;

	static class ListItemView {
		public TextView overhaulPlanItemCoding, overhaulPlanItemState,
				overhaulPlanItemName, overhaulPlanItemContent;
	}

	/**
	 * 实例化Adapter
	 * 
	 * @param context
	 * @param data
	 * @param resource
	 */
	public OverhaulProjectAdapter(Context context, List<OverhaulProject> data,
			int resource) {
		this.context = context;
		this.listContainer = LayoutInflater.from(context);
		this.listItems = data;
		this.itemViewResource = resource;
	}

	@Override
	public int getCount() {
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		return listItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void refresh(List<OverhaulProject> list) {
		listItems = list;
		this.notifyDataSetChanged();
	}

	public List<OverhaulProject> getlist() {
		return listItems;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ListItemView listItemView;
		if (convertView == null) {
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(this.itemViewResource, null);

			listItemView = new ListItemView();

			listItemView.overhaulPlanItemCoding = (TextView) convertView
					.findViewById(R.id.overhaul_item_project_coding);
			listItemView.overhaulPlanItemState = (TextView) convertView
					.findViewById(R.id.overhaul_project_item_state);
			listItemView.overhaulPlanItemName = (TextView) convertView
					.findViewById(R.id.overhaul_project_item_name);
			listItemView.overhaulPlanItemContent = (TextView) convertView
					.findViewById(R.id.overhaul_project_item_content);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		OverhaulProject OverhaulProject = listItems.get(position);
		String code = OverhaulProject.getWorkOrder_CD();
		listItemView.overhaulPlanItemCoding.setText(code);
		listItemView.overhaulPlanItemName.setText(OverhaulProject
				.getWorkOrderName_TX());
		listItemView.overhaulPlanItemContent.setText(OverhaulProject
				.getWorkOrderContent_TX());

		listItemView.overhaulPlanItemState.setTextColor(Color.BLACK);
		
		if (OverhaulProject.getFinish_YN().equalsIgnoreCase("1")) {
			listItemView.overhaulPlanItemState
				.setText(context
						.getString(R.string.overhaul_project_examine_isFinished));
			listItemView.overhaulPlanItemState
				.setTextColor(context
						.getResources()
						.getColor(
								R.color.xstblue));
		} else {
			switch (OverhaulProject.getJXType_ID()) {
			case 1:
				if (StringUtils.isEmpty(OverhaulProject.getQC0())) {
					listItemView.overhaulPlanItemState
					.setText(context
							.getString(R.string.overhaul_project_implement_unFinished));
				} else {
					listItemView.overhaulPlanItemState
					.setText(context
							.getString(R.string.overhaul_project_examine_one_unFinished));
				}
				break;
			case 2:
				if (StringUtils.isEmpty(OverhaulProject.getQC0())) {
					listItemView.overhaulPlanItemState
						.setText(context
								.getString(R.string.overhaul_project_implement_unFinished));
				} else {
					if (StringUtils.isEmpty(OverhaulProject.getQC1())) {
						listItemView.overhaulPlanItemState
							.setText(context
									.getString(R.string.overhaul_project_examine_one_unFinished));
					} else {
						listItemView.overhaulPlanItemState
							.setText(context
									.getString(R.string.overhaul_project_examine_two_unFinished));
					}
				}
				break;
			case 3:
				if (StringUtils.isEmpty(OverhaulProject.getQC0())) {
					listItemView.overhaulPlanItemState
						.setText(context
								.getString(R.string.overhaul_project_implement_unFinished));
				} else {
					if (StringUtils.isEmpty(OverhaulProject.getQC1())) {
						listItemView.overhaulPlanItemState
							.setText(context
									.getString(R.string.overhaul_project_examine_one_unFinished));
					} else {
						if (StringUtils.isEmpty(OverhaulProject.getQC2())) { 
							listItemView.overhaulPlanItemState
								.setText(context
										.getString(R.string.overhaul_project_examine_two_unFinished));
						} else {
							listItemView.overhaulPlanItemState
								.setText(context
										.getString(R.string.overhaul_project_examine_three_unFinished));
						}
					}
				}
				break;
			}
			
		}
		
		/*switch (OverhaulProject.getJXType_ID()) {
		case 1:
			if (StringUtils.isEmpty(OverhaulProject.getQC0())) {
				listItemView.overhaulPlanItemState
						.setText(context
								.getString(R.string.overhaul_project_implement_unFinished));
			} else {
				if (StringUtils.isEmpty(OverhaulProject.getQC1())) {
					listItemView.overhaulPlanItemState
							.setText(context
									.getString(R.string.overhaul_project_examine_one_unFinished));
				} else {
					listItemView.overhaulPlanItemState
							.setText(context
									.getString(R.string.overhaul_project_examine_isFinished));
					listItemView.overhaulPlanItemState.setTextColor(context
							.getResources().getColor(
									R.color.xstblue));
				}
			}
			break;
		case 2:
			if (StringUtils.isEmpty(OverhaulProject.getQC0())) {
				listItemView.overhaulPlanItemState
						.setText(context
								.getString(R.string.overhaul_project_implement_unFinished));
			} else {
				if (StringUtils.isEmpty(OverhaulProject.getQC1())) {
					listItemView.overhaulPlanItemState
							.setText(context
									.getString(R.string.overhaul_project_examine_one_unFinished));
				} else {
					if (StringUtils.isEmpty(OverhaulProject.getQC2())) {
						listItemView.overhaulPlanItemState
								.setText(context
										.getString(R.string.overhaul_project_examine_two_unFinished));
					} else {
						listItemView.overhaulPlanItemState
								.setText(context
										.getString(R.string.overhaul_project_examine_isFinished));
						listItemView.overhaulPlanItemState.setTextColor(context
								.getResources().getColor(
										R.color.xstblue));
					}
				}
			}
			break;
		case 3:
			if (StringUtils.isEmpty(OverhaulProject.getQC0())) {
				listItemView.overhaulPlanItemState
						.setText(context
								.getString(R.string.overhaul_project_implement_unFinished));
			} else {
				if (StringUtils.isEmpty(OverhaulProject.getQC1())) {
					listItemView.overhaulPlanItemState
							.setText(context
									.getString(R.string.overhaul_project_examine_one_unFinished));
				} else {
					if (StringUtils.isEmpty(OverhaulProject.getQC2())) {
						listItemView.overhaulPlanItemState
								.setText(context
										.getString(R.string.overhaul_project_examine_two_unFinished));
					} else {
						if (StringUtils.isEmpty(OverhaulProject.getQC3())) {
							listItemView.overhaulPlanItemState
									.setText(context
											.getString(R.string.overhaul_project_examine_three_unFinished));
						} else {
							listItemView.overhaulPlanItemState
									.setText(context
											.getString(R.string.overhaul_project_examine_isFinished));
							listItemView.overhaulPlanItemState
									.setTextColor(context
											.getResources()
											.getColor(
													R.color.xstblue));
						}
					}
				}

			}
			break;
		default:
			break;
		}*/
		return convertView;
	}
	/*
	 * @SuppressLint("ResourceAsColor") @Override public View getView(int
	 * position, View convertView, ViewGroup parent) { ListItemView
	 * listItemView; if (convertView == null) { // 获取list_item布局文件的视图
	 * convertView = listContainer.inflate(this.itemViewResource, null);
	 * 
	 * listItemView = new ListItemView();
	 * 
	 * listItemView.overhaulPlanItemCoding = (TextView) convertView
	 * .findViewById(R.id.overhaul_item_coding);
	 * listItemView.overhaulPlanItemState = (TextView) convertView
	 * .findViewById(R.id.overhaul_item_state);
	 * listItemView.overhaulPlanItemName = (TextView) convertView
	 * .findViewById(R.id.overhaul_item_name);
	 * listItemView.overhaulPlanItemContent = (TextView) convertView
	 * .findViewById(R.id.overhaul_item_content);
	 * convertView.setTag(listItemView); } else { listItemView = (ListItemView)
	 * convertView.getTag(); } OverhaulProject OverhaulProject =
	 * listItems.get(position); String code = OverhaulProject.getWorkOrder_CD();
	 * listItemView.overhaulPlanItemCoding.setText(code);
	 * listItemView.overhaulPlanItemName.setText(OverhaulProject
	 * .getWorkOrderName_TX());
	 * listItemView.overhaulPlanItemContent.setText(OverhaulProject
	 * .getWorkOrderContent_TX());
	 * 
	 * listItemView.overhaulPlanItemState.setTextColor(Color.BLACK); switch
	 * (OverhaulProject.getJXType_ID()) { case 1: if
	 * (StringUtils.isEmpty(OverhaulProject.getQC0())) {
	 * listItemView.overhaulPlanItemState .setText(context
	 * .getString(R.string.overhaul_project_implement_unFinished)); } else { if
	 * (
	 * OverhaulHelper.getCurrentQC(AppConst.CurrentQC.QC0.toString(),OverhaulProject
	 * )) { listItemView.overhaulPlanItemState .setText(context
	 * .getString(R.string.overhaul_project_implement_isFinished));
	 * listItemView.
	 * overhaulPlanItemState.setTextColor(context.getResources().getColor
	 * (R.color.overhaul_project_listitem)); } else { if
	 * (StringUtils.isEmpty(OverhaulProject.getQC1())) {
	 * listItemView.overhaulPlanItemState .setText(context
	 * .getString(R.string.overhaul_project_examine_one_unFinished)); } else {
	 * listItemView.overhaulPlanItemState .setText(context
	 * .getString(R.string.overhaul_project_examine_one_isFinished));
	 * listItemView
	 * .overhaulPlanItemState.setTextColor(context.getResources().getColor
	 * (R.color.overhaul_project_listitem)); } } } break; case 2: if
	 * (StringUtils.isEmpty(OverhaulProject.getQC0())) {
	 * listItemView.overhaulPlanItemState .setText(context
	 * .getString(R.string.overhaul_project_implement_unFinished)); } else { if
	 * (
	 * OverhaulHelper.getCurrentQC(AppConst.CurrentQC.QC0.toString(),OverhaulProject
	 * )) { listItemView.overhaulPlanItemState .setText(context
	 * .getString(R.string.overhaul_project_implement_isFinished));
	 * listItemView.
	 * overhaulPlanItemState.setTextColor(context.getResources().getColor
	 * (R.color.overhaul_project_listitem)); } else { if
	 * (StringUtils.isEmpty(OverhaulProject.getQC1())) {
	 * listItemView.overhaulPlanItemState .setText(context
	 * .getString(R.string.overhaul_project_examine_one_unFinished)); } else {
	 * if (OverhaulHelper.getCurrentQC(AppConst.CurrentQC.QC1.toString(),
	 * OverhaulProject)) { listItemView.overhaulPlanItemState .setText(context
	 * .getString(R.string.overhaul_project_examine_one_isFinished));
	 * listItemView
	 * .overhaulPlanItemState.setTextColor(context.getResources().getColor
	 * (R.color.overhaul_project_listitem)); } else { if
	 * (StringUtils.isEmpty(OverhaulProject.getQC2())) {
	 * listItemView.overhaulPlanItemState .setText(context
	 * .getString(R.string.overhaul_project_examine_two_unFinished)); } else {
	 * listItemView.overhaulPlanItemState .setText(context
	 * .getString(R.string.overhaul_project_examine_two_isFinished));
	 * listItemView
	 * .overhaulPlanItemState.setTextColor(context.getResources().getColor
	 * (R.color.overhaul_project_listitem)); } } } }
	 * 
	 * } break; case 3: if (StringUtils.isEmpty(OverhaulProject.getQC0())) {
	 * listItemView.overhaulPlanItemState .setText(context
	 * .getString(R.string.overhaul_project_implement_unFinished)); } else { if
	 * (
	 * OverhaulHelper.getCurrentQC(AppConst.CurrentQC.QC0.toString(),OverhaulProject
	 * )) { listItemView.overhaulPlanItemState .setText(context
	 * .getString(R.string.overhaul_project_implement_isFinished));
	 * listItemView.
	 * overhaulPlanItemState.setTextColor(context.getResources().getColor
	 * (R.color.overhaul_project_listitem)); } else { if
	 * (StringUtils.isEmpty(OverhaulProject.getQC1())) {
	 * listItemView.overhaulPlanItemState .setText(context
	 * .getString(R.string.overhaul_project_examine_one_unFinished)); } else {
	 * if (OverhaulHelper.getCurrentQC(AppConst.CurrentQC.QC1.toString(),
	 * OverhaulProject)) { listItemView.overhaulPlanItemState .setText(context
	 * .getString(R.string.overhaul_project_examine_one_isFinished));
	 * listItemView
	 * .overhaulPlanItemState.setTextColor(context.getResources().getColor
	 * (R.color.overhaul_project_listitem)); } else { if
	 * (StringUtils.isEmpty(OverhaulProject.getQC2())) {
	 * listItemView.overhaulPlanItemState .setText(context
	 * .getString(R.string.overhaul_project_examine_two_unFinished)); } else {
	 * if (OverhaulHelper.getCurrentQC(AppConst.CurrentQC.QC2.toString(),
	 * OverhaulProject)) { listItemView.overhaulPlanItemState .setText(context
	 * .getString(R.string.overhaul_project_examine_two_isFinished));
	 * listItemView
	 * .overhaulPlanItemState.setTextColor(context.getResources().getColor
	 * (R.color.overhaul_project_listitem)); } else { if
	 * (StringUtils.isEmpty(OverhaulProject .getQC3())) {
	 * listItemView.overhaulPlanItemState .setText(context
	 * .getString(R.string.overhaul_project_examine_three_unFinished)); } else {
	 * listItemView.overhaulPlanItemState .setText(context
	 * .getString(R.string.overhaul_project_examine_three_isFinished));
	 * listItemView
	 * .overhaulPlanItemState.setTextColor(context.getResources().getColor
	 * (R.color.overhaul_project_listitem)); } } } } }
	 * 
	 * } } break; default: break; } return convertView; }
	 */
}
