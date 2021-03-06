package com.moons.xst.track.bean;

import java.io.Serializable;

import com.google.zxing.client.result.ISBNParsedResult;
import com.moons.xst.track.R.string;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table JX_WORK_ORDER_FOR_HNYN.
 */
public class OverhaulProject implements BaseSearch,Serializable{

    private int WorkOrderForHNYN_ID; 
    private String WorkOrder_CD;
    private Integer ZYX_ID;
    private Integer Mobject_ID;
    private Integer Spec_ID;
    private Integer DJOwner_ID;
    private Integer CZDept_ID;
    private Integer JXDept_ID;
    private Integer JXType_ID;
    private String WorkOrderName_TX;
    private String WorkOrderContent_TX;
    private String PlanStart_DT;
    private String PlanEnd_DT;
    private Integer JXPlan_ID;
    private String HNYNType_TX;
    private Integer TACHE_ID;
    private String QC0;
    private String QC0_TX;
    private String QC0_MemoTX;
    private String QC1;
    private String QC1_TX;
    private String QC1_MemoTX;
    private String QC2;
    private String QC2_TX;
    private String QC2_MemoTX;
    private String QC3;
    private String QC3_TX;
    private String QC3_MemoTX ;
    private String Finish_YN;
    private String state;
    private String CurrentQC_TX;

    public OverhaulProject() {
    }

    public OverhaulProject(int WORKORDERFORHNYN_ID, String WorkOrder_CD, Integer ZYX_ID,
    		Integer Mobject_ID, Integer Spec_ID, Integer DJOwner_ID, Integer CZDept_ID, 
    		Integer JXDept_ID, Integer JXType_ID, String WorkOrderName_TX, 
    		String WorkOrderContent_TX, String PlanStart_DT, String PlanEnd_DT, 
    		Integer JXPlan_ID, String HNYNType_TX,Integer TACHE_ID,
    		String QC0, String QC0_TX, String QC0_MemoTX, String QC1, String QC1_TX,
    		String QC1_MemoTX, String QC2, String QC2_TX, String QC2_MemoTX, 
    		String QC3, String QC3_TX, String QC3_MemoTX , String Finish_YN,
    		String CurrentQC_TX) {
        this.WorkOrderForHNYN_ID = WORKORDERFORHNYN_ID;
        this.WorkOrder_CD = WorkOrder_CD;
        this.ZYX_ID = ZYX_ID;
        this.Mobject_ID = Mobject_ID;
        this.Spec_ID = Spec_ID;
        this.DJOwner_ID = DJOwner_ID;
        this.CZDept_ID = CZDept_ID;
        this.JXDept_ID = JXDept_ID;
        this.JXType_ID = JXType_ID;
        this.WorkOrderName_TX = WorkOrderName_TX;
        this.WorkOrderContent_TX = WorkOrderContent_TX;
        this.PlanStart_DT = PlanStart_DT;
        this.PlanEnd_DT = PlanEnd_DT;
        this.JXPlan_ID = JXPlan_ID;
        this.HNYNType_TX = HNYNType_TX;
        this.TACHE_ID = TACHE_ID;
        this.QC0 = QC0;
        this.QC0_TX = QC0_TX;
        this.QC0_MemoTX = QC0_MemoTX;
        this.QC1 = QC1;
        this.QC1_TX = QC1_TX;
        this.QC1_MemoTX = QC1_MemoTX;
        this.QC2 = QC2;
        this.QC2_TX = QC2_TX;
        this.QC2_MemoTX = QC2_MemoTX;
        this.QC3 = QC3;
        this.QC3_TX = QC3_TX;
        this.QC3_MemoTX  = QC3_MemoTX ;
        this.Finish_YN = Finish_YN;
        this.CurrentQC_TX=CurrentQC_TX;
    }

    public int getWorkOrderForHNYN_ID() {
        return WorkOrderForHNYN_ID;
    }

    public void setWorkOrderForHNYN_ID(int workOrderForHNYN_ID) {
        this.WorkOrderForHNYN_ID = workOrderForHNYN_ID;
    }

    public String getWorkOrder_CD() {
        return WorkOrder_CD;
    }

    public void setWorkOrder_CD(String WorkOrder_CD) {
        this.WorkOrder_CD = WorkOrder_CD;
    }

    public Integer getZYX_ID() {
        return ZYX_ID;
    }

    public void setZYX_ID(Integer ZYX_ID) {
        this.ZYX_ID = ZYX_ID;
    }

    public Integer getMobject_ID() {
        return Mobject_ID;
    }

    public void setMobject_ID(Integer Mobject_ID) {
        this.Mobject_ID = Mobject_ID;
    }

    public Integer getSpec_ID() {
        return Spec_ID;
    }

    public void setSpec_ID(Integer Spec_ID) {
        this.Spec_ID = Spec_ID;
    }

    public Integer getDJOwner_ID() {
        return DJOwner_ID;
    }

    public void setDJOwner_ID(Integer DJOwner_ID) {
        this.DJOwner_ID = DJOwner_ID;
    }

    public Integer getCZDept_ID() {
        return CZDept_ID;
    }

    public void setCZDept_ID(Integer CZDept_ID) {
        this.CZDept_ID = CZDept_ID;
    }

    public Integer getJXDept_ID() {
        return JXDept_ID;
    }

    public void setJXDept_ID(Integer JXDept_ID) {
        this.JXDept_ID = JXDept_ID;
    }

    public Integer getJXType_ID() {
        return JXType_ID;
    }

    public void setJXType_ID(Integer JXType_ID) {
        this.JXType_ID = JXType_ID;
    }

    public String getWorkOrderName_TX() {
        return WorkOrderName_TX;
    }

    public void setWorkOrderName_TX(String WorkOrderName_TX) {
        this.WorkOrderName_TX = WorkOrderName_TX;
    }

    public String getWorkOrderContent_TX() {
        return WorkOrderContent_TX;
    }

    public void setWorkOrderContent_TX(String WorkOrderContent_TX) {
        this.WorkOrderContent_TX = WorkOrderContent_TX;
    }

    public String getPlanStart_DT() {
        return PlanStart_DT;
    }

    public void setPlanStart_DT(String PlanStart_DT) {
        this.PlanStart_DT = PlanStart_DT;
    }

    public String getPlanEnd_DT() {
        return PlanEnd_DT;
    }

    public void setPlanEnd_DT(String PlanEnd_DT) {
        this.PlanEnd_DT = PlanEnd_DT;
    }

    public int getJXPlan_ID() {
        return JXPlan_ID;
    }

    public void setJXPlan_ID(int JXPlan_ID) {
        this.JXPlan_ID = JXPlan_ID;
    }

    public String getHNYNType_TX() {
        return HNYNType_TX;
    }

    public void setHNYNType_TX(String HNYNType_TX) {
        this.HNYNType_TX = HNYNType_TX;
    }
    
    public Integer getTACHE_ID() {
    	return TACHE_ID;
    }
    
    public void setTACHE_ID(Integer tache_id) {
    	TACHE_ID = tache_id;
    }
    
    public String getQC0() {
        return QC0;
    }

    public void setQC0(String QC0) {
        this.QC0 = QC0;
    }

    public String getQC0_TX() {
        return QC0_TX;
    }

    public void setQC0_TX(String QC0_TX) {
        this.QC0_TX = QC0_TX;
    }

    public String getQC0_MemoTX() {
        return QC0_MemoTX;
    }

    public void setQC0_MemoTX(String QC0_MemoTX) {
        this.QC0_MemoTX = QC0_MemoTX;
    }
    public String getQC1() {
        return QC1;
    }

    public void setQC1(String QC1) {
        this.QC1 = QC1;
    }

    public String getQC1_TX() {
        return QC1_TX;
    }

    public void setQC1_TX(String QC1_TX) {
        this.QC1_TX = QC1_TX;
    }

    public String getQC1_MemoTX() {
        return QC1_MemoTX;
    }

    public void setQC1_MemoTX(String QC1_MemoTX) {
        this.QC1_MemoTX = QC1_MemoTX;
    }

    public String getQC2() {
        return QC2;
    }

    public void setQC2(String QC2) {
        this.QC2 = QC2;
    }

    public String getQC2_TX() {
        return QC2_TX;
    }

    public void setQC2_TX(String QC2_TX) {
        this.QC2_TX = QC2_TX;
    }

    public String getQC2_MemoTX() {
        return QC2_MemoTX;
    }

    public void setQC2_MemoTX(String QC2_MemoTX) {
        this.QC2_MemoTX = QC2_MemoTX;
    }

    public String getQC3() {
        return QC3;
    }

    public void setQC3(String QC3) {
        this.QC3 = QC3;
    }

    public String getQC3_TX() {
        return QC3_TX;
    }

    public void setQC3_TX(String QC3_TX) {
        this.QC3_TX = QC3_TX;
    }

    public String getQC3_MemoTX () {
        return QC3_MemoTX ;
    }

    public void setQC3_MemoTX (String QC3_MemoTX ) {
        this.QC3_MemoTX  = QC3_MemoTX ;
    }

    public String getFinish_YN() {
        return Finish_YN;
    }

    public void setFinish_YN(String Finish_YN) {
        this.Finish_YN = Finish_YN;
    }
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    
    public String getCurrentQC() {
        return CurrentQC_TX;
    }

    public void setCurrentQC(String currentQC) {
        this.CurrentQC_TX = currentQC;
    }
    
    @Override
	public String getSearchCondition() {
		 StringBuilder searchStr = new StringBuilder();
	        searchStr.append(WorkOrder_CD);
	        searchStr.append(WorkOrderName_TX);
	        searchStr.append(WorkOrderContent_TX);
	        return searchStr.toString();
	}
}
