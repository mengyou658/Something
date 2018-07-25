create table DJ_ResultActive (
   DJ_Plan_ID           nvarchar(400)        not null,
   CompleteTime_DT      datetime             not null,
   AppUser_CD           nvarchar(30)         not null,
   AppUserName_TX       nvarchar(20)         null,
   Result_TX            nvarchar(100)        not null,
   Data8K_TX            image                null,
   Query_DT             datetime             not null,
   MObjectStateName_TX  nvarchar(20)         null,
   Exception_YN         nchar(1)             not null,
   AlarmLevel_ID        integer              null,
   SpecCase_YN          nchar(1)             not null,
   SpecCase_TX          nvarchar(400)        null,
   Memo_TX              nvarchar(160)        null,
   Time_NR              integer              not null,
   DataFlag_CD          nchar(1)             not null,
   ShiftName_TX         nvarchar (20)        null,
   ShiftGroupName_TX    nvarchar(6)          null,
   Ratio_NR             real				 null,
   Ratio1_NR            real                 null,
   Rate                 int                  null,
   DataLen              int                  null,
   LastResult_TX	    nvarchar(100)        null,
   TransInfo_TX		    nvarchar(500)        null,
   VibParam_TX			nvarchar(100)		 null,
   FeatureValue_TX		nvarchar(400)        null,
   FFTData_TX			image                null,
   GPSInfo_TX           nvarchar(30)         null,
   constraint PK_DJ_RESULTHIS primary key  (DJ_Plan_ID, CompleteTime_DT)
)
go

create table DJ_TaskIDPosActive (
   IDPos_ID             nvarchar(400)        not null,
   IDPosStart_TM        datetime             not null,
   IDPosEnd_TM          datetime             null,
   Query_DT             datetime             null,
   AppUser_CD           nvarchar(30)         not null,
   AppUserName_TX       nvarchar(20)         null,
   ShiftName_TX         nvarchar(6)          null,
   ShiftGroupName_TX    nvarchar(6)          null,
   TimeCount_NR         integer              not null,
   TransInfo_TX         nvarchar(500)        null,
   GPSInfo_TX           nvarchar(30)         null,
   constraint PK_DJ_TASKIDPOSACTIVE primary key (IDPos_ID, IDPosStart_TM)
)
go

create table MObject_State (
   StartAndEndPoint_ID  nvarchar(400)        not null,
   CompleteTime_DT      datetime             not null,
   MObjectState_CD      nvarchar(20)         null,
   MObjectStateName_TX  nvarchar(20)         not null,
   AppUser_CD           nvarchar(30)         null,
   DataState_YN         nchar(1)             null,
   TransInfo_TX         nvarchar(500)        null,
   constraint PK_MOBJECT_STATE primary key (StartAndEndPoint_ID, CompleteTime_DT)
)
go


create table RH_ResultActive (
   RHTask_ID            nvarchar(400)        not null,
   Memo_TX              nvarchar(1000)       null,
   DealWithUser_ID      integer              null,
   DealWith_DT          datetime             not null,
   constraint PK_RH_RESULTACTIVE primary key (RHTask_ID, DealWith_DT)
)
go

create table DJ_PhotoByResult (
   DJ_Plan_ID           nvarchar(400)        not null,
   Partition_ID         nvarchar(14)         not null,
   Photo_DT             DateTime             not null,
   GUID                 nvarchar(100)        not null,
   LCType				nvarchar(40)         not null,
   constraint PK_DJ_PhotoByResult primary key (DJ_Plan_ID, Photo_DT)
)
go

create table XS_Result (
Point_CD			nvarchar(100)      not null,
Point_ID		    nvarchar(400)	   null,
AppUser_ID		    nvarchar(400)	   not null,
AppUserName_TX		nvarchar(20)	   not null,
StateName_TX		nvarchar(20)	   not null,
Longitude_TX		nvarchar(20)	   not null,
Latitude_TX		    nvarchar(20)	   not null,
XSDate_DT	    	datetime	       not null,
Result_ID           nvarchar(20)       not null,
constraint PK_XS_Result primary key (XSDate_DT)
)
go

create table XS_ResultForRelationship (
Point_CD		   nvarchar(100)       not null,
Point_ID		   nvarchar(400)	       null,
Partition_ID       nvarchar(20)        not null,
GUID               nvarchar(100)       not null,
File_Byte          image                   null,
File_DT            datetime            not null,
File_Type          nchar(1)            not null,
Result_ID          nvarchar(20)        not null,
constraint PK_XS_ResultForRelationship primary key (GUID)
)
go

Create Table GPSPosition (
XJQGUID_TX         nvarchar(50)        not null,
XJQ_CD             nvarchar(20)            null,
Line_ID            nvarchar(150)           null,
AppUser_CD         nvarchar(30)            null,
AppUserName_TX     nvarchar(20)            null,
ID_CD              nvarchar(20)            null,
Place_TX           nvarchar(60)            null,
Longitude_TX       nvarchar(150)       not null,
Latitude_TX        nvarchar(150)       not null,
UTC_DT             datetime            not null
)
go

Create Table Z_Relation (
Int_ID         nvarchar(400)           not null,
String_ID             nvarchar(400)    not null,
RelationType            nvarchar(400)  not null,
Expand_TX         nvarchar(500)        not null
)
go

create table GPSPositionForInit  
(
       GPSPosition_ID  int                             not null,
       GPSPosition_CD  nvarchar(50)                    not null,
       XJQMark         nvarchar(150)                   not null,
       Longitude       nvarchar(150)                   not null,
       Latitude        nvarchar(150)                   not null,
       UTCDateTime     nvarchar(150)                   not null,
       GPSDesc         nvarchar(150)                   not null,
       LineID          nvarchar(150)                   not null
)
go

create table GPSPositionForResult 
(
       GPSPosition_ID  int                             not null,
       GPSPosition_CD  nvarchar(50)                    not null,
       XJQMark         nvarchar(150)                   not null,
       Longitude       nvarchar(150)                   not null,
       Latitude        nvarchar(150)                   not null,
       UTCDateTime     nvarchar(150)                   not null,
       GPSDesc         nvarchar(150)                   not null,
       LineID          nvarchar(150)                   not null,
       AppUser_ID      int                             not null,
       AppUserName_TX  nvarchar(40)                    not null,
       Post_ID         int                             not null
)
go

create table GPSMobjectBugResult
(
       Result_ID       nvarchar(20)                    not null,
       Type_ID         int                             not null,
       GZDM_ID         int                             not null,      
       ZZDM_ID         int                             not null,
       YYDM_ID         int                             not null,
       CSDM_ID         int                             not null,
       GPSPoint_ID     int                             not null,
       BugMemo_TX      nvarchar(1000),
       Find_TM         datetime                        not null,
       Post_ID         int                             not null,
       FindUser_TX     nvarchar(40)                    not null,
       Longitude       nvarchar(150)                   not null,
       Latitude        nvarchar(150)                   not null
)
go

create table GPSMobjectBugResultForFiles
(
       Result_ID       nvarchar(20)                    not null,
       GUID            nvarchar(100)                   not null,
       File_DT         datetime                        not null,
       File_Type       nchar(5)                        not null,
       constraint PK_GPSMobjectBugResultForFiles primary key (GUID)
)
go

