
/* 
 ================================================
 PVII Scroll To Top scripts
 Copyright (c) 2014 Project Seven Development
 www.projectseven.com
 Version: 1.0.7 -build 05
 ================================================
 
*/

var p7STT={
	ctl: [],
	defAnim: 1,
	defDuration: 450,
	status: false,
	once: false,
	body: null,
	prf: 'none',
	trsnd: '',
	animDelay: (1000/60)
};
function P7_STTset(){
	var i,h,sh='',ie=P7_STTgetIEver();
	if(!document.getElementById || (ie>4 && ie<6)){
		return;
	}
	sh+='div.p7STT {display:none;}\n';
	if(document.styleSheets){
		if(ie>4 && ie<8){
		}
		h='\n<st' + 'yle type="text/css">\n'+sh+'\n</s' + 'tyle>';
		document.write(h);
	}
	else{
		P7_STTaddSheet(sh);
	}
}
P7_STTset();
function P7_STTop(){
	if(!document.getElementById){
		return;
	}
	p7STT.ctl[p7STT.ctl.length]=arguments;
}
function P7_STTbb(){
}
function P7_STTaddLoad(){
	var ie=P7_STTgetIEver();
	if(!document.getElementById || (ie>4 && ie<6)){
		return;
	}
	if(window.addEventListener){
		window.addEventListener("load",P7_STTinit,false);
		window.addEventListener("unload",P7_STTbb,false);
		window.addEventListener("resize",P7_STTrsz,false);
		window.addEventListener('scroll', P7_STTcheck, false);
	}
	else if(window.attachEvent){
		document.write("<script id=p7ie_stt defer src=\"//:\"><\/script>");
		document.getElementById("p7ie_stt").onreadystatechange=function(){
			if (this.readyState=="complete"){
				if(p7STT.ctl.length>0){
					P7_STTinit();
				}
			}
		};
		window.attachEvent("onload",P7_STTinit);
		window.attachEvent("onunload",P7_STTbb);
		window.attachEvent("onresize",P7_STTrsz);
		window.attachEvent('onscroll',P7_STTcheck);
	}
}
P7_STTaddLoad();
function P7_STTinit(){
	var i,j,ie,tD,tr,el,tA,tP,iM,sr,x,s1,s2;
	if(p7STT.once){
		return;
	}
	p7STT.once=true;
	p7STT.body=document.body.parentNode;
	if(/KHTML|WebKit/i.test(navigator.userAgent) || P7_STTgetIEver()==5 ){
		p7STT.body=document.body;
	}
	tA=P7_STTgetElementsByClassName('p7STT-scroll-to-top');
	if(tA && tA.length){
		for(i=0;i<tA.length;i++){
			tA[i].onclick=function(){
				return P7_STTscrollToTop();
			};
		}
	}
	for(j=0;j<p7STT.ctl.length;j++){
		tD=document.getElementById(p7STT.ctl[j][0]);
		if(tD){
			tD.p7opt=p7STT.ctl[j];
			if(tD.parentNode.nodeName!='BODY'){
				document.getElementsByTagName('BODY')[0].appendChild(tD);
			}
			tD.sttStatus='off';
			P7_STTremClass(tD,'p7STTnoscript');
			tA=document.getElementById(tD.id.replace('_','a_'));
			if(tA){
				tA.sttDiv=tD;
				tA.onclick=function(){
					return P7_STTscrollToTop(this);
				};
			}
		}
	}
	P7_STTcheck();
}
function P7_STTscrollToTop(a){
	var st,dy,op;
	if(p7STT.body.p7AnimRunning){
		return false;
	}
	st=p7STT.body.scrollTop;
	if(st===0){
		return false;
	}
	op=(a && a.sttDiv)?a.sttDiv.p7opt[2]:p7STT.defAnim;
	dy=(a && a.sttDiv)?a.sttDiv.p7opt[3]:p7STT.defDuration;
	if(op==1){
		P7_STTscrollAnim(p7STT.body,st,0,dy,'quad');
	}
	else{
		p7STT.body.scrollTop=0;
	}
	return false;
}
function P7_STTrsz(){
	P7_STTcheck();
}
function P7_STTcheck(){
	var i,tB,st=0;
	if(!p7STT.body){
		return;
	}
	if(p7STT.body.p7AnimRunning){
		return;
	}
	if(p7STT.ctl && p7STT.ctl.length){
		st=p7STT.body.scrollTop;
		for(i=0;i<p7STT.ctl.length;i++){
			tB=document.getElementById(p7STT.ctl[i][0]);
			if(tB){
				if(st > tB.p7opt[1]){
					if(tB.sttStatus!='on'){
						tB.sttStatus='on';
						tB.style.display='block';
						if(window.addEventListener){
							tB.offsetWidth=tB.offsetWidth;
						}
						P7_STTsetClass(tB,'p7stt-on');
					}
				}
				else{
					if(tB.sttStatus!='off'){
						tB.sttStatus='off';
						P7_STTremClass(tB,'p7stt-on');
					}
				}
			}
		}
	}
}
function P7_STTgetTime(st){
	var d = new Date();
	return d.getTime() - st;
}
function P7_STTanim(tp,t,b,c,d){
	if(tp=='quad'){
		if((t/=d/2)<1){
			return c/2*t*t+b;
		}
		else{
			return -c/2*((--t)*(t-2)-1)+b;
		}
	}
	else{
		return (c*(t/d))+b;
	}
}
function P7_STTscrollAnim(ob,fv,tv,dur,typ,cb){
	if(ob.p7AnimRunning){
		ob.p7AnimRunning=false;
		clearInterval(ob.p7STTanim);
	}
	typ=(!typ)?'quad':typ;
	ob.p7animType=typ;
	ob.p7animStartVal=fv;
	ob.p7animCurrentVal=ob.p7animStartVal;
	ob.p7animFinishVal=tv;
	ob.p7animStartTime=P7_STTgetTime(0);
	ob.p7animDuration=dur;
	if(!ob.p7AnimRunning){
		ob.p7AnimRunning=true;
ob.p7STTanim=setInterval(function(){
	P7_STTscrollAnimator(ob,cb);
}
, p7STT.animDelay);
}
}
function P7_STTscrollAnimator(el,cb,op){
	var i,tB,tA,tS,et,nv,m=false;
	et=P7_STTgetTime(el.p7animStartTime);
	if(et>=el.p7animDuration){
		et=el.p7animDuration;
		m=true;
	}
	if(el.p7animCurrentVal!=el.p7animFinishVal){
		nv=P7_STTanim(el.p7animType, et, el.p7animStartVal, el.p7animFinishVal-el.p7animStartVal, el.p7animDuration);
		el.p7animCurrentVal=nv;
		el.scrollTop=nv;
	}
	if(m){
		el.p7AnimRunning=false;
		clearInterval(el.p7STTanim);
		P7_STTcheck();
		if(cb && typeof(cb) === "function"){
			cb.call(el);
		}
	}
}
function P7_STTsetClass(ob,cl){
	if(ob){
		var cc,nc,r=/\s+/g;
		cc=ob.className;
		nc=cl;
		if(cc&&cc.length>0){
			if(cc.indexOf(cl)==-1){
				nc=cc+' '+cl;
			}
			else{
				nc=cc;
			}
		}
		nc=nc.replace(r,' ');
		ob.className=nc;
	}
}
function P7_STTremClass(ob,cl){
	if(ob){
		var cc,nc;
		cc=ob.className;
		if(cc&&cc.indexOf(cl>-1)){
			nc=cc.replace(cl,'');
			nc=nc.replace(/\s+/g,' ');
			nc=nc.replace(/\s$/,'');
			nc=nc.replace(/^\s/,'');
			ob.className=nc;
		}
	}
}
function P7_STTgetElementsByClassName(cls){
	var i,x=0,aL,aT,rS=[];
	if(typeof(document.getElementsByClassName)!='function'){
		aL=document.getElementsByTagName('*');
		for(i=0;i<aL.length;i++){
			aT=aL[i].className;
			if(aT&&aT==cls){
				rS[x]=aL[i];
				x++;
			}
		}
	}
	else{
		rS=document.getElementsByClassName(cls);
	}
	return rS;
}
function P7_STTgetIEver(){
	var j,v=-1,nv,m=false;
	nv=navigator.userAgent.toLowerCase();
	j=nv.indexOf("msie");
	if(j>-1){
		v=parseFloat(nv.substring(j+4,j+8));
		if(document.documentMode){
			v=document.documentMode;
		}
	}
	return v;
}
