/* USE WORDWRAP & MAXIMIZE THE WINDOW TO SEE THIS FILE
========================================
 SmartMenus v5.5.4 Script LOADER
 Commercial License No.: UN-LICENSED
========================================
 Please note: THIS IS NOT FREE SOFTWARE.
 Available licenses for use:
 http://www.smartmenus.org/license.php
========================================
 (c)2006 ET VADIKOM-VASIL DINKOV
========================================
 IE4+ NS4+ Opera5+ Konqueror2+ Safari
========================================
 Get the menus with a great manual & examples at:
 http://www.smartmenus.org
 LEAVE THESE NOTES PLEASE - delete the comments if you want */


s_The_Arrays_Source='s_arrays.js';// the source of the menu definitions
s_Script_DOM_Source='s_script_dom.js';// the source of your script
s_Script_NS4_Source='s_script_ns4.js';
s_Script_OLD_Source='s_script_old.js';
s_Any_Add_On_Source='';// if you need any add-on

// ===
s_x=document;s_ua=navigator.userAgent.toLowerCase();s_dl=s_x.getElementById?1:0;s_iE=s_x.all&&!window.innerWidth&&s_ua.indexOf("msie")!=-1?1:0;s_oP=s_ua.indexOf("opera")!=-1&&s_x.clear?1:0;s_oP7=s_oP&&s_x.appendChild?1:0;s_oP7m=s_oP&&!s_oP7?1:0;s_nS4=s_x.layers?1:0;s_kNv=s_ua.indexOf("konqueror")!=-1?parseFloat(s_ua.substring(s_ua.indexOf("konqueror/")+10)):0;s_kN=s_kNv>=2.2?1:0;s_kN3p=s_kNv>=3?1:0;s_kN31p=s_kNv>=3.1?1:0;s_kN32p=s_kNv>=3.2?1:0;s_nS=s_dl&&!s_x.all&&s_ua.indexOf("opera")==-1&&!s_kN?1:0;s_mC=s_ua.indexOf("mac")!=-1?1:0;s_sF=s_mC&&s_ua.indexOf("safari")!=-1?1:0;s_eS=s_ua.indexOf("escape")!=-1?1:0;s_iE5M=s_mC&&s_iE&&s_dl&&!s_eS?1:0;s_iE4=!s_mC&&s_iE&&!s_dl?1:0;s_a="undefined";s_iE7=s_iE&&typeof(XMLHttpRequest)!=s_a?1:0;s_ct=0;var s_ML,s_AL;s_=[""];s_1=[];s_2=[];s_c="<script language=JavaScript1.2 src='";s_e="' type=text/javascript></script>";function s_add(a,b){s_ct++;s_[s_ct]=[];s_[s_ct][0]={IEF:a.S[17],B:a.S[3],BC:a.S[0],BCNS4:a.S[1],BW:a.S[2],PD:a.S[4],SC:a.S[27],SCT:a.S[28],SCB:a.S[29],SCW:a.S[30],SCH:a.S[31]};for(var k in a)s_[s_ct][0][k]=a[k];for(var i=0;i<b.length;i++){if(s_autoSELECTED){if(b[i].Show&&b[i].Show!=""){if(typeof s_1[b[i].Show]==s_a)s_1[b[i].Show]=[];s_1[b[i].Show][s_1[b[i].Show].length]=[s_ct,i+1]}var u,h,t;h=location.href;u=b[i].U;t=h.substring(h.length-u.length);if(t==u&&u!=""){s_2[s_2.length]=a.N;b[i].SELECTED=true;if(!s_autoSELECTEDItemsClickable)b[i].U=""}}s_[s_ct][i+1]={BgColor:b[i].SELECTED?a.S[24]:a.S[5],OverBgColor:a.S[6],FontColor:b[i].SELECTED?a.S[25]:a.S[7],OverFontColor:a.S[8],FontFamily:a.S[9],FontSize:a.S[10],FontSizeNS4:a.S[11],FontWeight:a.S[12],TextAlign:a.S[13],Padding:a.S[14],SeparatorSize:a.S[15],SeparatorColor:a.S[16],UseSubImg:a.S[18],SubImgSrc:b[i].SELECTED?a.S[26]:a.S[19],OverSubImgSrc:a.S[20],SubImgWidth:a.S[21],SubImgHeight:a.S[22],SubImgTop:a.S[23],Target:s_target,Class:b[i].SELECTED?a.S[34]:a.S[32],OverClass:b[i].SELECTED?a.S[34]:a.S[33],BorderColor:b[i].SELECTED?a.S[38]:a.S[36],OverBorderColor:a.S[37],BorderWidth:a.S[35]?a.S[35]:0,SeparatorSpacing:a.S[39]?a.S[39]:0,SeparatorBgImage:a.S[40],BgImage:b[i].SELECTED?a.S[43]:a.S[41],OnBgImage:a.S[42]};for(var j in b[i])s_[s_ct][i+1][j]=b[i][j];if(s_[s_ct][i+1].BgImage){s_[s_ct][i+1].FS=new Image;s_[s_ct][i+1].FS.src=s_[s_ct][i+1].BgImage}if(s_[s_ct][i+1].OnBgImage){s_[s_ct][i+1].NS=new Image;s_[s_ct][i+1].NS.src=s_[s_ct][i+1].OnBgImage}if(b[i].Image){s_[s_ct][i+1].Fs=new Image;s_[s_ct][i+1].Fs.src=b[i].Image[0]}if(b[i].OnImage){s_[s_ct][i+1].Ns=new Image;s_[s_ct][i+1].Ns.src=b[i].OnImage}}};s_tm1=s_c+s_The_Arrays_Source+s_e;s_tm2=s_c+(s_nS4?s_Script_NS4_Source:s_iE&&!s_dl||s_kN&&!s_kN3p||s_oP7m?s_Script_OLD_Source:s_Script_DOM_Source)+s_e;s_tm3=s_Any_Add_On_Source!=""?s_c+s_Any_Add_On_Source+s_e:"";if(s_iE||s_nS4||s_nS||s_oP){s_x.write(s_tm1);s_x.write(s_tm2);if(s_tm3!="")s_x.write(s_tm3)}if(s_kN)s_x.write(s_tm1+s_tm2+s_tm3);