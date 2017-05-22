package supersql.codegenerator.infinitescroll;

import java.io.File;

import supersql.codegenerator.DecorateList;
import supersql.codegenerator.ITFE;
import supersql.codegenerator.Incremental;
import supersql.codegenerator.LinkForeach;
import supersql.codegenerator.Sass;
import supersql.codegenerator.TFE;
import supersql.codegenerator.Compiler.Compiler;
import supersql.codegenerator.HTML.HTMLEnv;
import supersql.codegenerator.Mobile_HTML5.Mobile_HTML5;
import supersql.codegenerator.Mobile_HTML5.Mobile_HTML5Attribute;
import supersql.codegenerator.Mobile_HTML5.Mobile_HTML5C1;
import supersql.codegenerator.Mobile_HTML5.Mobile_HTML5C2;
import supersql.codegenerator.Mobile_HTML5.Mobile_HTML5Env;
import supersql.codegenerator.Mobile_HTML5.Mobile_HTML5Function;
import supersql.codegenerator.Mobile_HTML5.Mobile_HTML5G1;
import supersql.codegenerator.Mobile_HTML5.Mobile_HTML5G2;
import supersql.codegenerator.Mobile_HTML5.Mobile_HTML5Manager;
import supersql.codegenerator.Mobile_HTML5.Mobile_HTML5_dynamic;
import supersql.codegenerator.Mobile_HTML5.Mobile_HTML5_form;
import supersql.codegenerator.Mobile_HTML5.Mobile_HTML5_show;
import supersql.common.GlobalEnv;
import supersql.common.Log;
import supersql.extendclass.ExtList;
import supersql.parser.Embed;

public class Infinitescroll {

	InfiniteEnv html_Env;
	static StringBuffer codes;


	static String[] formSql = {"","delete","update","insert","login","logout"};
	static String[] formHtml = {"","submit","select","checkbox","radio","text","textarea","hidden"};
	static int whichForm;

	public static void Attributes(Mobile_HTML5Attribute ATT, Mobile_HTML5Env html_env, Mobile_HTML5Env html_env2, ExtList data_info){
		String classid_for_ifs = "";
		String[] ifs_div_String = {"", ""};
		codes = new StringBuffer();
		StringBuffer tmp = new StringBuffer();


		html_env.code = Embed.preProcess(html_env.code, ATT.decos);	//goto 20130915-2  "<$  $>"

		String classid = Mobile_HTML5Env.getClassID(ATT);

		//changed by goto 20161113  for function class width
		setWidth(classid, ATT.decos, html_env);

		if(GlobalEnv.isOpt()){
			//			work_opt(data_info);
		}else{
			if(Mobile_HTML5Env.getFormItemFlg() && Mobile_HTML5Env.getFormItemName().equals(formHtml[2])){

			}else{

				Mobile_HTML5.preProcess("Mobile_HTML5Attribute", ATT.decos, html_env);	//Pre-process (前処理)	//TODO この位置でOK?

				//20130309
				//20130309
				//20130314 table
				//20130409
				if((Mobile_HTML5C1.tableFlg||Mobile_HTML5C1.table0Flg||Mobile_HTML5G1.tableFlg||Mobile_HTML5G1.table0Flg||
						Mobile_HTML5C2.tableFlg||Mobile_HTML5C2.table0Flg||Mobile_HTML5G2.tableFlg||Mobile_HTML5G2.table0Flg||
						ATT.decos.containsKey("table") || ATT.decos.containsKey("table0"))
						&& (!Mobile_HTML5C1.divFlg&&!Mobile_HTML5C2.divFlg&&!Mobile_HTML5G1.divFlg&&!Mobile_HTML5G2.divFlg)){
					html_env.code.append("<table width=\"100%\"" + html_env.getOutlineModeAtt() + " ");
					html_env.code.append("class=\"att");

					tmp.append("<table width=\"100%\"" + html_env.getOutlineModeAtt() + " ");
					tmp.append("class=\"att");

					if(html_env.written_classid.contains(classid)){
						//classを持っているとき(ex.TFE10000)のみ指定 
						html_env.code.append(" " + classid);
						tmp.append(" " + classid);
					}
					if(ATT.decos.containsKey("class")){ 
						//classを持っているとき(ex.TFE10000)のみ指定 
						html_env.code.append(" " + ATT.decos.getStr("class"));
						tmp.append(" " + ATT.decos.getStr("class"));
					}
					html_env.code.append("\"");
					html_env.code.append(">");

					tmp.append("\"");
					tmp.append(">");
				}
			}

			if(Mobile_HTML5Env.getFormItemFlg()){

			}else{
				//20130309
				//20130409
				//if(decos.containsKey("table") || decos.containsKey("table0"))	html_env.code.append("<tr><td>\n");		//20130314 table
				if((Mobile_HTML5C1.tableFlg||Mobile_HTML5C1.table0Flg||Mobile_HTML5G1.tableFlg||Mobile_HTML5G1.table0Flg||
						Mobile_HTML5C2.tableFlg||Mobile_HTML5C2.table0Flg||Mobile_HTML5G2.tableFlg||Mobile_HTML5G2.table0Flg||
						ATT.decos.containsKey("table") || ATT.decos.containsKey("table0"))
						&& (!Mobile_HTML5C1.divFlg&&!Mobile_HTML5C2.divFlg&&!Mobile_HTML5G1.divFlg&&!Mobile_HTML5G2.divFlg)){
					html_env.code.append("<tr><td>\n");		//20130314 table
					tmp.append("<tr><td>\n");
				}
				Log.out("<table class=\"att\"><tr><td>");
			}

			if (html_env.link_flag > 0 || html_env.sinvoke_flag) {

				//tk start for draggable div///////////////////////////////////////
				if(html_env.draggable)
				{	
					html_env.code.append("<div id=\""+html_env.dragdivid+"\" class=\"draggable\"");
					tmp.append("<div id=\""+html_env.dragdivid+"\" class=\"draggable\"");
					Log.out("<div id=\""+html_env.dragdivid+"\" ");
				}	
				else{
					//tk end for draggable div/////////////////////////////////////////
					if(html_env.isPanel){
						html_env.code.append("<div id=\"container\">");
						tmp.append("<div id=\"container\">");
					}
					//added by goto 20120614 start
					//[%連結子] 下記の2つの問題があったため、hrefの指定を絶対パスから「相対パス形式」へ変更
					//1.絶対パスだとFirefoxではリンク先が開けない
					//2.ITCの実習環境ではリンク先が開けない
					String fileDir = new File(html_env.linkurl).getAbsoluteFile().getParent();
					if(fileDir.length() < html_env.linkurl.length()
							&& fileDir.equals(html_env.linkurl.substring(0,fileDir.length()))){
						String relative_path = html_env.linkurl.substring(fileDir.length()+1);
						html_env.code.append("<A href=\"" + relative_path + "\" ");
						tmp.append("<A href=\"" + relative_path + "\" ");
					}else
						//changed by goto 20161019 for new foreach
						//added by goto 20161109 for plink/glink
						if(html_env.plink_glink_onclick.isEmpty()){
							html_env.code.append("<A href=\"" + html_env.linkurl + "\" data-ajax=\"false\" ");
							tmp.append("<A href=\"" + html_env.linkurl + "\" data-ajax=\"false\" ");
						}else{
							html_env.code.append("<A href=\"\" onclick=\""+LinkForeach.ID1+"("+html_env.plink_glink_onclick+"); return false;\" data-ajax=\"false\" ");
							tmp.append("<A href=\"\" onclick=\""+LinkForeach.ID1+"("+html_env.plink_glink_onclick+"); return false;\" data-ajax=\"false\" ");
						}
					
					//html_env.code.append("<A href=\"" + html_env.linkurl + "\" ");
					//added by goto 20120614 end
				}

				//added by goto 20121217 start
				//画面遷移アニメーション (data-transition)
				//transition = fade, slide, pop, slideup, slidedown, flip
				if (ATT.decos.containsKey("transition")){
					html_env.code.append("data-transition=\"" + ATT.decos.getStr("transition") + "\" ");
					tmp.append("data-transition=\"" + ATT.decos.getStr("transition") + "\" ");
					//System.out.println(decos.getStr("transition"));
				}
				//added by goto 20121217 end


				//tk start//////////////////////////////////////////////////////////
				if(ATT.decos.containsKey("target")){
					html_env.code.append(" target=\"" + ATT.decos.getStr("target")+"\"");
					tmp.append(" target=\"" + ATT.decos.getStr("target")+"\"");
				}
				if(ATT.decos.containsKey("class")){
					html_env.code.append(" class=\"" + ATT.decos.getStr("class") + "\"");
					tmp.append(" class=\"" + ATT.decos.getStr("class") + "\"");
				}

				if(GlobalEnv.isAjax() && html_env.isPanel)
				{
					html_env.code.append(" onClick =\"return panel('Panel','"+html_env.ajaxquery+"'," +
							"'"+html_env.dragdivid+"','"+html_env.ajaxcond+"')\"");
					tmp.append(" onClick =\"return panel('Panel','"+html_env.ajaxquery+"'," +
							"'"+html_env.dragdivid+"','"+html_env.ajaxcond+"')\"");
				}
				else if(GlobalEnv.isAjax() && !html_env.draggable)
				{
					String target = GlobalEnv.getAjaxTarget();
					if(target == null)
					{
						String query = html_env.ajaxquery;
						if (query.indexOf(".sql")>0) {
							if (query.contains("/")) {
								target = query.substring(query.lastIndexOf("/")+1,query.indexOf(".sql"));
							} else {
								target = query.substring(0,query.indexOf(".sql"));
							}
						} else if (query.indexOf(".ssql")>0) {
							if (query.contains("/")) {
								target = query.substring(query.lastIndexOf("/")+1,query.indexOf(".ssql"));
							} else {
								target = query.substring(0,query.indexOf(".ssql"));
							}
						}

						if(html_env.has_dispdiv)
						{
							target = html_env.ajaxtarget;
						}
						Log.out("a target:"+target);
					}
					html_env.code.append(" onClick =\"return loadFile('"+html_env.ajaxquery+"','"+target+
							"','"+html_env.ajaxcond+"',"+html_env.inEffect+","+html_env.outEffect+")\"");
					tmp.append(" onClick =\"return loadFile('"+html_env.ajaxquery+"','"+target+
							"','"+html_env.ajaxcond+"',"+html_env.inEffect+","+html_env.outEffect+")\"");

				}


				html_env.code.append(">\n");
				tmp.append(">\n");
				//tk end////////////////////////////////////////////////////////////

				Log.out("<A href=\"" + html_env.linkurl + "\">");
			}

			//Log.out("data_info: "+this.getStr(data_info));
			Mobile_HTML5.beforeWhileProcess("Mobile_HTML5Attribute", ATT.decos, html_env);
			Mobile_HTML5.whileProcess1_2("Mobile_HTML5Attribute", ATT.decos, html_env, null, data_info, null, null, -1);	//TODO ここでOK?

			createForm(data_info, ATT, html_env, html_env2, tmp);


			if(whichForm == 0){ //normal process (not form)
				//***APPEND DATABASE VALUE***//
				Log.out(data_info);
				if(Mobile_HTML5_dynamic.dynamicDisplay || Mobile_HTML5_form.form){
					//20131118 dynamic
					if(Mobile_HTML5_dynamic.dynamicDisplay){
						html_env.code.append( Mobile_HTML5_dynamic.dynamicAttributeProcess(ATT, html_env) );
						tmp.append( Infinite_dynamic.dynamicAttributeProcess(ATT, html_env) );
					}
					//20131127 form
					if(Mobile_HTML5_form.form){
						html_env.code.append( Mobile_HTML5_form.formAttributeProcess(ATT, ATT.decos) );
						tmp.append( Mobile_HTML5_form.formAttributeProcess(ATT, ATT.decos) );
					}

				}else{
					//					if(!Sass.isBootstrapFlg()){
					html_env.code.append(ATT.getStr(data_info));
					tmp.append(ATT.getStr(data_info));
					//					}else if(Sass.isBootstrapFlg()){
					//						html_env.code.append("<div class=\"" + classid +"\">");
					//						html_env.code.append(this.getStr(data_info));
					//						html_env.code.append("</div>");
					//						if(Sass.outofloopFlg.peekFirst()){
					//		        			Sass.makeClass(classid);
					//		        			Sass.defineGridBasic(classid, decos);
					//		        			Sass.closeBracket();
					//			      		}
					//					}
				}

				Mobile_HTML5.whileProcess2_1("Mobile_HTML5Attribute", ATT.decos, html_env, null, data_info, null, null, -1);	//TODO ここでOK?
				Mobile_HTML5.afterWhileProcess("Mobile_HTML5Attribute", classid, ATT.decos, html_env);

				if (html_env.link_flag > 0 || html_env.sinvoke_flag) {
					if(html_env.draggable){
						html_env.code.append("</div>\n");
						tmp.append("</div>\n");
					}else
					{
						html_env.code.append("</A>\n");
						tmp.append("</A>\n");

						if(html_env.isPanel){
							html_env.code.append("</div>\n");
							tmp.append("</div>\n");
						}
					}
					Log.out("</A>");
				}


				if(Mobile_HTML5Env.getFormItemFlg() && Mobile_HTML5Env.getFormItemName().equals(formHtml[2])){

				}else{
					html_env.code.append("\n");			//20130309
					if((Mobile_HTML5C1.tableFlg||Mobile_HTML5C1.table0Flg||Mobile_HTML5G1.tableFlg||Mobile_HTML5G1.table0Flg||
							Mobile_HTML5C2.tableFlg||Mobile_HTML5C2.table0Flg||Mobile_HTML5G2.tableFlg||Mobile_HTML5G2.table0Flg||
							ATT.decos.containsKey("table") || ATT.decos.containsKey("table0"))
							&& (!Mobile_HTML5C1.divFlg&&!Mobile_HTML5C2.divFlg&&!Mobile_HTML5G1.divFlg&&!Mobile_HTML5G2.divFlg)){
						html_env.code.append("</td></tr></table>\n");	//20130314 table
						tmp.append("</td></tr></table>\n");
					}
					Log.out("</td></tr></table>");
				}


				Mobile_HTML5.postProcess("Mobile_HTML5Attribute", classid, ATT.decos, html_env);	//Post-process (後処理)

				Log.out("TFEId = " + classid);
			}
		}
		codes = tmp;

	}
	public static void C1(Mobile_HTML5C1 C1, Mobile_HTML5Env html_env, ExtList data_info, ExtList data, ExtList<TFE> tfes, int tfeItems){
		String outType = "div";

		String classid_for_ifs = "";
		String[] ifs_div_String = {"", ""};
		codes = new StringBuffer();
		StringBuffer tmp = new StringBuffer();

		String classid = Mobile_HTML5Env.getClassID(C1);

		if(C1.decos.containsKey("insert")){
			Mobile_HTML5Env.setIDU("insert");
		}	
		if(C1.decos.containsKey("update")){
			Mobile_HTML5Env.setIDU("update");
		}
		if(C1.decos.containsKey("delete")){
			Mobile_HTML5Env.setIDU("delete");
		}
		html_env.append_css_def_td(classid, C1.decos);

		//20130325  table0
		if(C1.decos.containsKey("table0"))	C1.table0Flg = true;
		else							C1.table0Flg = false;
		//20130314  table
		if(C1.decos.containsKey("table") || C1.table0Flg || Mobile_HTML5C2.tableFlg || Mobile_HTML5G1.tableFlg || Mobile_HTML5G2.tableFlg){
			C1.tableFlg = true;
		}//else	tableFlg = false;

		//20130326  div
		if(C1.decos.containsKey("div")){
			C1.divFlg = true;
			C1.tableFlg = false;
		}//else divFlg = false;

		//20161203 bootstrap
		if(Sass.isFirstElementFlg()){
			C1.firstFlg = true;
			Sass.firstElementFlg(false);
		}

		if(!GlobalEnv.isOpt()){
			//20160914 bootstrap
			if(!Sass.isBootstrapFlg()){
				//20130503  Panel
				//				C1.panelFlg = panelProcess1(C1.decos, html_env);

				//20130330 tab
				//tab1
				if(C1.decos.containsKey("tab1")){
					html_env.code.append("<div data-role=\"content\"> <div id=\"tabs\">\n<ul>\n");
					html_env.code.append("	<li><a href=\"#tabs-"+Mobile_HTML5Env.tabCount+"\">");

					tmp.append("<div data-role=\"content\"> <div id=\"tabs\">\n<ul>\n");
					tmp.append("	<li><a href=\"#tabs-"+Mobile_HTML5Env.tabCount+"\">");

					if(!C1.decos.getStr("tab1").equals("")){
						html_env.code.append(C1.decos.getStr("tab1"));
						tmp.append(C1.decos.getStr("tab1"));
					}
					else{
						html_env.code.append("tab1");
						tmp.append("tab1");
					}
					html_env.code.append("</a></li>\n");
					html_env.code.append("</ul>\n<div id=\"tabs-"+Mobile_HTML5Env.tabCount+"\">\n");

					tmp.append("</a></li>\n");
					tmp.append("</ul>\n<div id=\"tabs-"+Mobile_HTML5Env.tabCount+"\">\n");
				}
				//tab2〜tab15
				else{
					int i=2;
					while(i<=Mobile_HTML5Env.maxTab){		//html_env.maxTab=15
						//Log.info("i="+i+" !!");
						if(C1.decos.containsKey("tab"+i) || (i==2 && C1.decos.containsKey("tab"))){
							//replace: </ul>の前に<li>〜</li>を付加
							String a = "</ul>";
							String b = "	<li><a href=\"#tabs-"+Mobile_HTML5Env.tabCount+"\">";
							if(C1.decos.containsKey("tab"+i))
								if(!C1.decos.getStr("tab"+i).equals(""))	b += C1.decos.getStr("tab"+i);
								else				            		b += "tab"+i;
							else
								if(!C1.decos.getStr("tab").equals(""))		b += C1.decos.getStr("tab");
								else				            		b += "tab";
							b += "</a></li>\n";
							Mobile_HTML5Manager.replaceCode(html_env, a, b+a);

							//replace: 最後の</div></div></div>カット
							Mobile_HTML5Manager.replaceCode(html_env, "</div></div></div>", "");

							//    	        		//replace: 不要な「<div class=〜」をカット
							//    	        		if(!Mobile_HTML5Manager.replaceCode(html_env, "<div class=\""+Mobile_HTML5Env.getClassID(this)+" \">", "")){
							//    	        		}

							html_env.code.append("<div id=\"tabs-"+Mobile_HTML5Env.tabCount+"\">\n");
							tmp.append("<div id=\"tabs-"+Mobile_HTML5Env.tabCount+"\">\n");
							break;
						}
						i++;
					}
				}

				//20130312 collapsible
				if(C1.decos.containsKey("collapse")){
					html_env.code.append("<DIV data-role=\"collapsible\" data-content-theme=\"c\" style=\"padding: 0px 12px;\">\n");
					tmp.append("<DIV data-role=\"collapsible\" data-content-theme=\"c\" style=\"padding: 0px 12px;\">\n");

					//header
					if(!C1.decos.getStr("collapse").equals("")){
						html_env.code.append("	<h1>"+C1.decos.getStr("collapse")+"</h1>\n");
						tmp.append("	<h1>"+C1.decos.getStr("collapse")+"</h1>\n");
					}else{
						html_env.code.append("<h1>Contents</h1>\n");
						tmp.append("<h1>Contents</h1>\n");
					}
				}

				//20130309
				//20160527 bootstrap
				if(!C1.tableFlg){
					//        		if(html_env.written_classid.contains(classid))
					html_env.code.append("<DIV Class=\"ui-grid #"+Mobile_HTML5Env.uiGridCount+" "+classid+"\"");
					tmp.append("<DIV Class=\"ui-grid #"+Mobile_HTML5Env.uiGridCount+" "+classid+"\"");
					//        		else
					//        			html_env.code.append("<DIV Class=\"ui-grid #"+Mobile_HTML5Env.uiGridCount+"\"");
					Mobile_HTML5Env.uiGridCount++;
				}
				if(!C1.tableFlg){
					html_env.code.append(">");		//20130309
					tmp.append(">");		//20130309
				}

				//20130314  table
				if(C1.tableFlg){
					//					html_env.code.append(getTableStartTag(html_env, C1.decos, C1)+"<TR>"); // taji comment
				}
			}//20160914 ifmobile

			//20160527 bootstrap
			else if(Sass.isBootstrapFlg()){
				if(!C1.tableFlg){
					if(C1.firstFlg){
						html_env.code.append("<DIV Class=\"row\">\n");
						html_env.code.append("<DIV Class=\""+classid+"\">\n");

						tmp.append("<DIV Class=\"row\">\n");
						tmp.append("<DIV Class=\""+classid+"\">\n");

						if(Sass.outofloopFlg.peekFirst()){
							//            				Sass.makeRowClass();
							//            				Sass.makeClass(classid);
							//            				Sass.defineGridBasic(classid, decos);

							//            				Sass.makeClass(classid);
							//            				Sass.defineGridBasic(classid, decos);
							//            				Sass.closeBracket();
							Sass.makeColumn(classid, C1.decos, "", -1);
						}
					}

					html_env.code.append("<DIV Class=\"row\">\n");
					tmp.append("<DIV Class=\"row\">\n");
					if(Sass.outofloopFlg.peekFirst()){
						//            			Sass.makeRowClass();
					}
				}

				if(C1.tableFlg){
					//					html_env.code.append(getTableStartTag(html_env, C1.decos, C1)+"<TR>"); //taji commentout
				}
			}

		}

		//			System.out.println("C1 tableFlg = " + tableFlg + ", divFlg = " + divFlg);
		int i = 0;
		if(Sass.outofloopFlg.peekFirst()){
			C1.gridMap = Sass.beforeC1WhileProcess(tfes);
			//        	Log.info(gridMap);
		}
		Mobile_HTML5.beforeWhileProcess(C1.getSymbol(), C1.decos, html_env);
		Infinite.beforeWhileProcess(C1.getSymbol(), C1.decos, html_env, ifs_div_String, classid_for_ifs, tmp);
		while (C1.hasMoreItems()) {
			ITFE tfe = (ITFE) tfes.get(i);
			DecorateList decos2 = ((TFE)tfe).decos;
			String classid2 = Mobile_HTML5Env.getClassID(tfe);
			C1.Count = ( (C1.gridInt>=C1.jj)? C1.jj:C1.gridInt );

			if(!Sass.isBootstrapFlg()){
				if(C1.decos.containsKey("table0") || Mobile_HTML5C2.table0Flg || Mobile_HTML5G1.table0Flg || Mobile_HTML5G2.table0Flg)	C1.table0Flg = true;
				if(C1.decos.containsKey("table") || Mobile_HTML5C2.tableFlg || Mobile_HTML5G1.tableFlg || Mobile_HTML5G2.tableFlg || C1.table0Flg)	C1.tableFlg=true;
				if(C1.decos.containsKey("div")){
					C1.divFlg = true;
					C1.tableFlg = false;
				}//else divFlg = false;

				if(!C1.tableFlg){	//div
					//20131002
					int tfesItemNum = tfes.size();
					Mobile_HTML5Attribute.attributeDivWidth = Mobile_HTML5.getDivWidth("C1", C1.decos, tfesItemNum - Mobile_HTML5Function.func_null_count);	//null()
					html_env.code.append("\n<div class=\"ui-block "+classid2+"\">\n");	//20130309
					tmp.append("\n<div class=\"ui-block "+classid2+"\">\n");
					//added by goto 20161113  for function class width
					Mobile_HTML5Attribute.setWidth(classid2, C1.decos, html_env);
				}
				//20130314  table
				if(C1.tableFlg){
					html_env.code.append("<TD valign=\"middle\" class=\"" + classid2 + " nest\">\n");
					tmp.append("<TD valign=\"middle\" class=\"" + classid2 + " nest\">\n");
				}
				if(C1.decos.containsKey("text")){
					Mobile_HTML5Function.textFlg2 = true;
				}
			}else if(Sass.isBootstrapFlg()){
				((TFE)tfe).decos.put("C1","Done");
				if(Sass.outofloopFlg.peekFirst()){
					if(C1.gridMap.get(classid2).containsKey("xs")){
						decos2.put("xs", ""+C1.gridMap.get(classid2).get("xs"));
					}
					if(C1.gridMap.get(classid2).containsKey("sm")){
						decos2.put("sm", ""+C1.gridMap.get(classid2).get("sm"));
					}
					if(C1.gridMap.get(classid2).containsKey("md")){
						decos2.put("md", ""+C1.gridMap.get(classid2).get("md"));
					}
					if(C1.gridMap.get(classid2).containsKey("lg")){
						decos2.put("lg", ""+C1.gridMap.get(classid2).get("lg"));
					}
				}
				html_env.code.append("<div class=\"" + classid2 +"\">\n");
				tmp.append("<div class=\"" + classid2 +"\">\n");
				//taji commentout
				//				if(Sass.outofloopFlg.peekFirst()){
				//					Sass.(classid2, decos2, C1.getSymbol(), C1.responsiveId);
				//				}
			}

			html_env.cNum++;
			html_env.xmlDepth++;
			Mobile_HTML5.whileProcess1_2(C1.getSymbol(), C1.decos, html_env, data, data_info, tfe, tfes, tfeItems);
			C1.worknextItem();
			tmp.append(codes.toString());
			Mobile_HTML5.whileProcess2_1(C1.getSymbol(), C1.decos, html_env, data, data_info, tfe, tfes, tfeItems);
			html_env.cNum--;
			html_env.xmlDepth--;
			if(C1.decos.containsKey("table0") || Mobile_HTML5C2.table0Flg || Mobile_HTML5G1.table0Flg || Mobile_HTML5G2.table0Flg)	C1.table0Flg = true;
			if(C1.decos.containsKey("table") || Mobile_HTML5C2.tableFlg || Mobile_HTML5G1.tableFlg || Mobile_HTML5G2.tableFlg || C1.table0Flg)	C1.tableFlg=true;
			if(C1.decos.containsKey("div")){
				C1.divFlg = true;
				C1.tableFlg = false;
			}//else divFlg = false;

			C1.ii++;
			C1.jj++;
			C1.gridInt++;
			if(Mobile_HTML5Function.func_null_count<1){	//null()
				if(!Sass.isBootstrapFlg()){
					if(!C1.tableFlg){
						html_env.code.append("</div>\n");	//20130309 20160527 bootstrap
						tmp.append("</div>\n");
					}
					if(C1.tableFlg){
						html_env.code.append("</TD>\n");	//20130314  table
						tmp.append("</TD>\n");	//20130314  table
					}
				}
				else if(Sass.isBootstrapFlg()){
					html_env.code.append("\n</DIV>\n");//.classid2
					tmp.append("\n</DIV>\n");//.classid2
					if(Sass.outofloopFlg.peekFirst()){
						//	            		Sass.closeBracket();//classid2
					}
				}
			}

			i++;

			Mobile_HTML5.whileProcess2_2(C1.getSymbol(), C1.decos, html_env, data, data_info, tfe, null, -1);

		}
		Infinite.afterWhileProcess(C1.getSymbol(), classid, C1.decos, html_env, ifs_div_String, classid_for_ifs, tmp);
		Mobile_HTML5.afterWhileProcess(C1.getSymbol(), classid, C1.decos, html_env);

		if(!Sass.isBootstrapFlg()){
			//20130309
			if(!C1.tableFlg){
				html_env.code.append("\n</DIV c1>\n");			//20130309
				tmp.append("\n</DIV c1>\n");
			}

			//20130314  table
			if(C1.tableFlg){
				html_env.code.append("</TR></TABLE>\n");	//20130309
				tmp.append("</TR></TABLE>\n");
				//	      		tableFlg = false;
				//	      		table0Flg = false;			//20130325 table0
			}

			//20130312 collapsible
			if(C1.decos.containsKey("collapse")){
				html_env.code.append("</DIV>");
				tmp.append("</DIV>");
			}

			//20130330 tab
			int a=1;
			while(a<=Mobile_HTML5Env.maxTab){
				//Log.info("a="+a);
				if(C1.decos.containsKey("tab"+a) || (a==1 && C1.decos.containsKey("tab"))){
					html_env.code.append("</div></div></div>\n");
					tmp.append("</div></div></div>\n");
					Mobile_HTML5Env.tabCount++;
					break;
				}
				a++;
			}

			//20130503  Panel
			//			panelProcess2(C1.decos, html_env, C1.panelFlg);

			C1.jj=0;

			if(C1.divFlg)	C1.divFlg = false;		//20130326  div
		}
		else if(Sass.isBootstrapFlg()){
			//        	html_env.code.append("\n</DIV>\n");//.row
			//        	html_env.code.append("\n</DIV>\n");//.TFE
			//      		if(Sass.outofloopFlg.peekFirst()){
			//      			Sass.closeBracket();
			//      			Sass.closeBracket();
			//      		}
			//      		if(!decos.containsKey("C1") && !decos.containsKey("G1")){
			//        		html_env.code.append("\n</DIV>\n");
			//        		if(Sass.outofloopFlg.peekFirst()){
			//        			Sass.closeBracket();
			//        		}
			//        	}
			html_env.code.append("</DIV>\n");//.row
			tmp.append("</DIV>\n");//.row
			if(Sass.outofloopFlg.peekFirst()){
				//        		Sass.closeBracket();//row
			}

			if(C1.firstFlg){
				html_env.code.append("</DIV>\n");//.classid
				html_env.code.append("</DIV>\n");//.row

				tmp.append("</DIV>\n");//.classid
				tmp.append("</DIV>\n");//.row

				if(Sass.outofloopFlg.peekFirst()){
					//        			Sass.closeBracket();//classid
					//        			Sass.closeBracket();//row
				}
				C1.firstFlg = false;
			}
		}
		Mobile_HTML5.postProcess(C1.getSymbol(), classid, C1.decos, html_env);	//Post-process (後処理)
		codes = tmp;
	}

	public static void C2(Mobile_HTML5C2 C2, Mobile_HTML5Env html_env, ExtList data_info, ExtList data, ExtList<TFE> tfes, int tfeItems){
		String outType = "div";
		String classid = Mobile_HTML5Env.getClassID(C2);

		String classid_for_ifs = "";
		String[] ifs_div_String = {"", ""};
		codes = new StringBuffer();
		StringBuffer tmp = new StringBuffer();

		//		if(html_env.xmlDepth!=0){
		//			// 親のoutTypeを継承
		//			outType = html_env.outTypeList.get(html_env.xmlDepth-1);
		//		}
		//		if (C2.decos.containsKey("table") || !outType.equals("div")) {
		//			html_env.outTypeList.add(html_env.xmlDepth, "table");
		//		} else {
		//			html_env.outTypeList.add(html_env.xmlDepth, "div");
		//		}
		//		if (C2.decos.containsKey("div")) {
		//			html_env.outTypeList.add(html_env.xmlDepth, "div");
		//		}
		//			System.out.println("C2 tableFlg = " + tableFlg + ", divFlg = " + divFlg);
		html_env.append_css_def_td(html_env.getClassID(C2), C2.decos);
		//modified by taji
		Mobile_HTML5.beforeWhileProcess(C2.getSymbol(), C2.decos, html_env);
		Infinite.beforeWhileProcess(C2.getSymbol(), C2.decos, html_env, ifs_div_String, classid_for_ifs, tmp);
		int i = 0;
		while (C2.hasMoreItems()) {
			ITFE tfe = (ITFE) tfes.get(i);
			DecorateList decos2 = ((TFE)tfe).decos;
			String classid2 = Mobile_HTML5Env.getClassID(tfe);
			if(!Sass.isBootstrapFlg()){
				if(C2.decos.containsKey("table0") || Mobile_HTML5C1.table0Flg || Mobile_HTML5G1.table0Flg || Mobile_HTML5G2.table0Flg)	C2.table0Flg = true;
				if(C2.decos.containsKey("table") || Mobile_HTML5C1.tableFlg || Mobile_HTML5G1.tableFlg || Mobile_HTML5G2.tableFlg || C2.table0Flg)	C2.tableFlg=true;
				if(C2.decos.containsKey("div")){
					C2.divFlg = true;
					C2.tableFlg = false;
				}//else divFlg = false;

				//20130312 collapsible
				if(C2.decos.containsKey("collapse")){
					html_env.code.append("<p>\n");
					tmp.append("<p>\n");
				}
				//20160527 bootstrap
				else if(!C2.tableFlg && !Mobile_HTML5Function.textFlg2){
					//20130309
					//					html_env.code.append("<div class=\""+classid2+" \">\n");
					html_env.code.append("<div class=\""+classid+" \">\n");
					tmp.append("<div class=\""+classid+" \">\n");
				}

				//20130314  table
				if(C2.tableFlg){
					html_env.code.append("<TR><TD valign=\"middle\" class=\""
							+ classid2 + " nest\">\n");
					tmp.append("<TR><TD valign=\"middle\" class=\""
							+ classid2 + " nest\">\n");
					Log.out("<TR><TD class=\"nest "
							+ classid2 + " nest\"> decos:" + C2.decos);
				}
			}else if(Sass.isBootstrapFlg()){
				html_env.code.append("<DIV Class=\"row\">\n");
				html_env.code.append("<div class=\"" + classid2 +"\">\n");

				tmp.append("<DIV Class=\"row\">\n");
				tmp.append("<div class=\"" + classid2 +"\">\n");

				if(Sass.outofloopFlg.peekFirst()){
					Sass.makeColumn(classid2, decos2, "", -1);
				}
			}

			html_env.cNum++;
			html_env.xmlDepth++;
			Mobile_HTML5.whileProcess1_2(C2.getSymbol(), C2.decos, html_env, data, data_info, tfe, tfes, tfeItems);
			C2.worknextItem();
			tmp.append(codes.toString());
			Mobile_HTML5.whileProcess2_1(C2.getSymbol(), C2.decos, html_env, data, data_info, tfe, tfes, tfeItems);
			if(!Sass.isBootstrapFlg()){
				if(C2.decos.containsKey("table0") || Mobile_HTML5C1.table0Flg || Mobile_HTML5G1.table0Flg || Mobile_HTML5G2.table0Flg)	C2.table0Flg = true;
				if(C2.decos.containsKey("table") || Mobile_HTML5C1.tableFlg || Mobile_HTML5G1.tableFlg || Mobile_HTML5G2.tableFlg || C2.table0Flg)	C2.tableFlg=true;
				if(C2.decos.containsKey("div")){
					C2.divFlg = true;
					C2.tableFlg = false;
				}//else divFlg = false;
				//20130314  table
				if(C2.tableFlg){
					html_env.code.append("</TD></TR>\n");
					tmp.append("</TD></TR>\n");
				}
				//Log.out("</TD></TR>");

				//20130312 collapsible
				if(C2.decos.containsKey("collapse")){
					html_env.code.append("</p>\n");
					tmp.append("</p>\n");
				}
				//20160527 bootstrap
				else if(!C2.tableFlg && !Mobile_HTML5Function.textFlg && !Mobile_HTML5Function.textFlg2){	//20130914  "text"
					html_env.code.append("\n</div>");
					tmp.append("\n</div>");
				}
				if(Mobile_HTML5Function.textFlg){					//20130914  "text"
					Mobile_HTML5Function.textFlg = false;
				}
			}else if(Sass.isBootstrapFlg()){
				html_env.code.append("</div>\n");//classid2
				html_env.code.append("</div>\n");//row

				tmp.append("</div>\n");//classid2
				tmp.append("</div>\n");//row
				if(Sass.outofloopFlg.peekFirst()){
				}
			}
			html_env.code.append("\n");		//20130309
			tmp.append("\n");

			html_env.cNum--;
			html_env.xmlDepth--;
			Mobile_HTML5.whileProcess2_2(C2.getSymbol(), C2.decos, html_env, data, data_info, tfe, null, -1);
			i++;
		}
		Infinite.afterWhileProcess(C2.getSymbol(), classid, C2.decos, html_env, ifs_div_String, classid_for_ifs, tmp);
		Mobile_HTML5.afterWhileProcess(C2.getSymbol(), classid, C2.decos, html_env);
		codes = tmp;
	}

	public static void G2(Mobile_HTML5G2 G2, Mobile_HTML5Env html_env, Mobile_HTML5Env html_env2, ExtList data_info, ExtList data, ITFE tfe){
		String classid = Mobile_HTML5Env.getClassID(G2);
		String classid2 = Mobile_HTML5Env.getClassID(tfe);
		String row = "";
		String column = "";
		String classid_for_ifs = "";
		String[] ifs_div_String = {"", ""};
		codes = new StringBuffer();
		StringBuffer tmp = new StringBuffer();
		if(!Infinite.preProcess(G2.getSymbol(), G2.decos, html_env)) return;	//Pre-process (前処理)


		if(G2.decos.containsKey("infinite-scroll")){
			classid_for_ifs = classid + "_wrapper";
			DecorateList deco_ifs = new DecorateList();
			deco_ifs.put("infinite-scroll", "");
			html_env.append_css_def_td(G2.getSymbol(), classid_for_ifs, classid2 ,deco_ifs);
			G2.decos.remove("infinite-scroll");
			ifs_div_String = Mobile_HTML5.ifs_div_start(G2.getSymbol(), html_env, classid_for_ifs, ifs_div_String);
		}
		// ページネーション
		if(Mobile_HTML5Env.getSelectFlg())
			data_info = (ExtList) data_info.get(0);
		html_env.append_css_def_td(classid, G2.decos);

		//20130325  table0
		if(G2.decos.containsKey("table0"))	Mobile_HTML5G2.table0Flg = true;
		else							Mobile_HTML5G2.table0Flg = false;
		//20130314  table
		if(G2.decos.containsKey("table") || Mobile_HTML5G2.table0Flg || Mobile_HTML5C1.tableFlg || Mobile_HTML5C2.tableFlg || Mobile_HTML5G1.tableFlg){
			Mobile_HTML5G2.tableFlg = true;
		}//else	tableFlg = false;

		//20130326  div
		if(G2.decos.containsKey("div")){
			Mobile_HTML5G2.divFlg = true;
			Mobile_HTML5G2.tableFlg = false;
		}//else divFlg = false;

		//20130914  "text"
		if(G2.decos.containsKey("text")){
			Mobile_HTML5Function.textFlg2 = true;
		}

		//20161203 bootstrap
		if(Sass.isFirstElementFlg()){
			G2.firstFlg = true;
			Sass.firstElementFlg(false);
		}

		if(!GlobalEnv.isOpt()){
			if(!Sass.isBootstrapFlg()){
				//20130503  Panel
				//				panelFlg = Mobile_HTML5C1.panelProcess1(G2.decos, html_env);

				//20130330 tab
				//tab1
				if(G2.decos.containsKey("tab1")){
					html_env.code.append("<div data-role=\"content\"> <div id=\"tabs\">\n<ul>\n");
					html_env.code.append("	<li><a href=\"#tabs-"+Mobile_HTML5Env.tabCount+"\">");
					if(!G2.decos.getStr("tab1").equals(""))	html_env.code.append(G2.decos.getStr("tab1"));
					else          							html_env.code.append("tab1");
					html_env.code.append("</a></li>\n");
					html_env.code.append("</ul>\n<div id=\"tabs-"+Mobile_HTML5Env.tabCount+"\">\n");
				}
				//tab2〜tab15
				else{
					int i=2;
					while(i<=Mobile_HTML5Env.maxTab){		//HTMLEnv.maxTab=15
						if(G2.decos.containsKey("tab"+i) || (i==2 && G2.decos.containsKey("tab"))){
							//replace: </ul>の前に<li>〜</li>を付加
							String a = "</ul>";
							String b = "	<li><a href=\"#tabs-"+Mobile_HTML5Env.tabCount+"\">";
							if(G2.decos.containsKey("tab"+i))
								if(!G2.decos.getStr("tab"+i).equals(""))	b += G2.decos.getStr("tab"+i);
								else				            		b += "tab"+i;
							else
								if(!G2.decos.getStr("tab").equals(""))		b += G2.decos.getStr("tab");
								else				            		b += "tab";
							b += "</a></li>\n";
							Mobile_HTML5Manager.replaceCode(html_env, a, b+a);

							//replace: 最後の</div></div></div>カット
							Mobile_HTML5Manager.replaceCode(html_env, "</div></div></div>", "");

							//    	        		//replace: 不要な「<div class=〜」をカット
							//    	        		Mobile_HTML5Manager.replaceCode(html_env, "<div class=\""+Mobile_HTML5Env.getClassID(this)+" \">", "");

							html_env.code.append("<div id=\"tabs-"+Mobile_HTML5Env.tabCount+"\">\n");
							break;
						}
						i++;
					}
				}

				//20130312 collapsible
				if(G2.decos.containsKey("collapse")){
					html_env.code.append("<DIV data-role=\"collapsible\" data-content-theme=\"c\" style=\"padding: 0px 12px;\">\n");

					//header
					if(!G2.decos.getStr("collapse").equals(""))
						html_env.code.append("	<h1>"+G2.decos.getStr("collapse")+"</h1>\n");
					else
						html_env.code.append("<h1>Contents</h1>\n");
				}

				//20130309
				//20130314  table
				if(Mobile_HTML5G2.tableFlg){
					if(G2.row>1 && Mobile_HTML5G2.tableFlg)	Mobile_HTML5G2.tableStartTag = Mobile_HTML5C1.getTableStartTag(html_env, G2.decos, G2);
					else					html_env.code.append(Mobile_HTML5C1.getTableStartTag(html_env, G2.decos, G2)+"\n");
				}
			}else if(Sass.isBootstrapFlg()){
				//        		if(!decos.containsKey("C1") && !decos.containsKey("G1")){
				//            		html_env.code.append("<DIV Class=\"row\">");
				//            		if(Sass.outofloopFlg.peekFirst()){
				//            			Sass.makeRowClass();
				//            		}
				//            	}
				//        		html_env.code.append("<DIV Class=\""+classid+"\">");
				//        		if(Sass.outofloopFlg.peekFirst()){
				//        			Sass.makeClass(classid);
				//        			Sass.defineGridBasic(classid, decos);
				//	      		}
				//        		Sass.beforeLoop();
				if(G2.firstFlg){
					html_env.code.append("<DIV Class=\"row\">\n");
					html_env.code.append("<DIV Class=\""+classid+"\">\n");

					tmp.append("<DIV Class=\"row\">\n");
					tmp.append("<DIV Class=\""+classid+"\">\n");

					if(Sass.outofloopFlg.peekFirst()){
						//        				Sass.makeRowClass();
						//        				Sass.makeClass(classid);
						//        				Sass.defineGridBasic(classid, decos);

						//        				Sass.makeClass(classid);
						//        				Sass.defineGridBasic(classid, decos);
						//        				Sass.closeBracket();
						Sass.makeColumn(classid, G2.decos, "", -1);
					}
				}
				Sass.beforeLoop();
			}
		}
		Mobile_HTML5.beforeWhileProcess(G2.getSymbol(), G2.decos, html_env, ifs_div_String);
		Infinite.beforeWhileProcess(G2.getSymbol(), G2.decos, html_env, ifs_div_String, classid_for_ifs, tmp);
		while (G2.hasMoreItems()) {
			// System.out.println("ここ: tableFlg = " + tableFlg +
			// ", divFlg = " + divFlg);
			Mobile_HTML5.gLevel1++;
			Mobile_HTML5.whileProcess1_1(G2.getSymbol(), G2.decos, html_env, data, data_info, tfe, null, -1);
			//			String classid2 = Mobile_HTML5Env.getClassID(tfe);
			DecorateList decos2 = ((TFE)tfe).decos;
			Mobile_HTML5Function.setGlvl(html_env.getGlevel());	//added by goto 20130914  "SEQ_NUM"
			//[重要] For [ [], ]!        	
			Mobile_HTML5G1.jj = 0;
			Mobile_HTML5G1.gridInt = 0;

			if(G2.decos.containsKey("table0") || Mobile_HTML5C1.table0Flg || Mobile_HTML5C2.table0Flg || Mobile_HTML5G1.table0Flg)	Mobile_HTML5G2.table0Flg = true;
			if(G2.decos.containsKey("table") || Mobile_HTML5C1.tableFlg || Mobile_HTML5C2.tableFlg || Mobile_HTML5G1.tableFlg || Mobile_HTML5G2.table0Flg)	Mobile_HTML5G2.tableFlg=true;
			if(G2.decos.containsKey("div")){
				Mobile_HTML5G2.divFlg = true;
				Mobile_HTML5G2.tableFlg = false;
			}

			html_env.setGlevel(html_env.getGlevel() + 1);
			if(G2.rowFlg){
				html_env.code = new StringBuffer();
				html_env.countfile++;
				html_env.filename = html_env.outfile + "_row" + G2.rowFileNum + "_" + G2.j + Compiler.getExtension();
				html_env.nextbackfile = html_env.linkoutfile + "_row" + G2.rowFileNum + "_" + G2.j + Compiler.getExtension();
				html_env.setOutlineMode();
			}
			if( Mobile_HTML5Env.getSelectRepeat() ){//if form_select
			}else{
				if(!Sass.isBootstrapFlg()){
					//20130312 collapsible
					if(G2.decos.containsKey("collapse")){
						html_env.code.append("<p>\n");
						tmp.append("<p>\n");
					}
					//20130309
					if(!Mobile_HTML5G2.tableFlg){
						html_env.code.append("\n<div class=\""+classid+" "+Mobile_HTML5_show.addShowCountClassName(G2.decos)+"\">\n");	//20130309  div
						tmp.append("\n<div class=\""+classid+" "+Mobile_HTML5_show.addShowCountClassName(G2.decos)+"\">\n");	//20130309  div
					}else if(Mobile_HTML5G2.tableFlg){
						//20130314  table
						html_env.code.append("<TR><TD class=\"" + classid + " "+Mobile_HTML5_show.addShowCountClassName(G2.decos)+" nest\">\n");
						tmp.append("<TR><TD class=\"" + classid + " "+Mobile_HTML5_show.addShowCountClassName(G2.decos)+" nest\">\n");
						Log.out("<TR><TD class=\"" + classid + " nest\">");
					}
				}else if(Sass.isBootstrapFlg()){
					html_env.code.append("<DIV Class=\"row\">\n");
					html_env.code.append("<div class=\"" + classid2 +"\">\n");

					tmp.append("<DIV Class=\"row\">\n");
					tmp.append("<div class=\"" + classid2 +"\">\n");

					if(Sass.outofloopFlg.peekFirst()){
						Sass.makeColumn(classid2, decos2, "", -1);
					}
				}
			}

			html_env.xmlDepth++;
			Mobile_HTML5.whileProcess1_2(G2.getSymbol(), G2.decos, html_env, data, data_info, tfe, null, -1);
			G2.worknextItem();
			tmp.append(codes.toString());
			//			Log.ehtmlInfo(classid_for_ifs);
			//			Log.ehtmlInfo(tmp);
			Mobile_HTML5.whileProcess2_1(G2.getSymbol(), G2.decos, html_env, data, data_info, tfe, null, -1);
			if(G2.decos.containsKey("table0") || Mobile_HTML5C1.table0Flg || Mobile_HTML5C2.table0Flg || Mobile_HTML5G1.table0Flg)	Mobile_HTML5G2.table0Flg = true;
			if(G2.decos.containsKey("table") || Mobile_HTML5C1.tableFlg || Mobile_HTML5C2.tableFlg || Mobile_HTML5G1.tableFlg || Mobile_HTML5G2.table0Flg)	Mobile_HTML5G2.tableFlg=true;
			if(G2.decos.containsKey("div")){
				Mobile_HTML5G2.divFlg = true;
				Mobile_HTML5G2.tableFlg = false;
			}

			html_env.xmlDepth--;
			if(Mobile_HTML5Env.getSelectRepeat()){

			}else{	 
				html_env2.code.append("</tfe>");
				if(!Sass.isBootstrapFlg()){
					if(!Mobile_HTML5G2.tableFlg){
						if(!Mobile_HTML5Function.textFlg2){
							html_env.code.append("</div>\n");		//20130309  div	//20130914  "text"
							tmp.append("</div>\n");
						}
					}else if(Mobile_HTML5G2.tableFlg){
						html_env.code.append("</TD></TR>\n");			//20130314  table
						Log.out("</TD></TR>");
					}
				}else if(Sass.isBootstrapFlg()){
					html_env.code.append("</div>\n");//classid2
					html_env.code.append("</div>\n");//row

					tmp.append("</div>\n");//classid2
					tmp.append("</div>\n");//row

					if(Sass.outofloopFlg.peekFirst()){
						//                		Sass.closeBracket();//classid2
						//                		Sass.closeBracket();//row
					}
				}

				html_env.code = Embed.postProcess(html_env.code);	//goto 20130915-2  "<$  $>"

				//20130312 collapsible
				if(G2.decos.containsKey("collapse")){
					html_env.code.append("</p>\n");
					tmp.append("</p>\n");
				}
			}
			//20160527 bootstrap
			if(Sass.isBootstrapFlg()){
				Sass.afterFirstLoop();
			}

			html_env.setGlevel(html_env.getGlevel() - 1);
			Mobile_HTML5G2.tableDivHeader_Count1++;	//20131001 tableDivHeader

			//added by goto 20130413  "row Prev/Next"
			if(G2.rowFlg){
				G2.codeBuf.append(html_env.code);
				if((G2.rowNum+1)%G2.row==0){
					Mobile_HTML5G2.createHTMLfile_ForPrevNext(html_env, G2.codeBuf);
					G2.j++;
					G2.codeBuf = new StringBuffer();
				}
				G2.rowNum++;
			}
			if(!Mobile_HTML5.whileProcess2_2(G2.getSymbol(), G2.decos, html_env, data, data_info, tfe, null, -1)){
				break;
			}

			Mobile_HTML5.gLevel1--;
		}
		Infinite.afterWhileProcess(G2.getSymbol(), classid, G2.decos, html_env, ifs_div_String, classid_for_ifs, tmp);
		Mobile_HTML5.afterWhileProcess(G2.getSymbol(), classid, G2.decos, html_env, ifs_div_String);
		if (Sass.isBootstrapFlg()){
			Sass.afterLoop();
			if(G2.firstFlg){
				html_env.code.append("</DIV>\n");//.classid
				html_env.code.append("</DIV>\n");//.row

				tmp.append("</DIV>\n");//.classid
				tmp.append("</DIV>\n");//.row

				if(Sass.outofloopFlg.peekFirst()){
					//        			Sass.closeBracket();//classid
					//        			Sass.closeBracket();//row
				}
				G2.firstFlg = false;
			}
		}

		//added by goto 20130413  "row Prev/Next"
		if(G2.rowFlg){
			if(G2.rowNum%G2.row!=0){	//最後の child HTML を create
				Mobile_HTML5G2.createHTMLfile_ForPrevNext(html_env, G2.codeBuf);
			}
			// taji todo
			//			html_env.filename = parentfile;
			//			html_env.code = parentcode;
			//			html_env.header = parentheader;
			//			html_env.footer = parentfooter;
			//			html_env.nextbackfile = parentnextbackfile;
			Log.out("TFEId = " + classid);
			html_env.append_css_def_td(classid, G2.decos);

			int first = 1, last = ((G2.rowNum%G2.row!=0)? (G2.rowNum/G2.row+1):(G2.rowNum/G2.row));
			Mobile_HTML5G2.PrevNextProcess(html_env, G2.rowNum, G2.row, first, last, 1);
		}

		if(Mobile_HTML5Env.getSelectRepeat()){		
			if(Mobile_HTML5Env.getSelectRepeat()){
				//chie
				html_env2.code.append("</select></VALUE></tfe>");
				html_env.code.append("</select></TD></TR>\n");
				Log.out("</TD></TR>");
				Mobile_HTML5Env.setSelectRepeat(false);
				Mobile_HTML5Env.incrementFormPartsNumber();
			}else{
				Mobile_HTML5Env.incrementFormPartsNumber();
			}
		}

		//20130314  table
		if(Mobile_HTML5G2.tableFlg){
			if(!(G2.row>1 && Mobile_HTML5G2.tableFlg))	html_env.code.append("</TABLE>\n");		//20130309
			Mobile_HTML5G2.tableFlg = false;
			Mobile_HTML5G2.table0Flg = false;		//20130325 table0
		}
		Log.out("</TABLE>");

		//20130312 collapsible
		if(G2.decos.containsKey("collapse")){
			html_env.code.append("</DIV>");
			tmp.append("</DIV>");
		}

		//20130330 tab
		int a=1;
		while(a<=Mobile_HTML5Env.maxTab){
			if(G2.decos.containsKey("tab"+a) || (a==1 && G2.decos.containsKey("tab"))){
				html_env.code.append("</div></div></div>\n");
				tmp.append("</div></div></div>\n");
				Mobile_HTML5Env.tabCount++;
				break;
			}
			a++;
		}

		//20130503  Panel
		//		Mobile_HTML5C1.panelProcess2(G2.decos, html_env, G2.panelFlg);

		if(Mobile_HTML5G2.divFlg)	Mobile_HTML5G2.divFlg = false;		//20130326  div

		//added by goto 20130413  "row Prev/Next"
		if(G2.rowFlg){
			G2.rowFileNum++;
			G2.rowFlg = false;
			G2.tableStartTag = "";
		}

		//20130914  "text"
		if(Mobile_HTML5Function.textFlg2){
			Mobile_HTML5Function.textFlg2 = false;
		}

		//        if(Sass.isBootstrapFlg()){
		//
		//        }
		codes.append(tmp.toString());
		Mobile_HTML5.postProcess(G2.getSymbol(), classid, G2.decos, html_env);	//Post-process (後処理)

		//added by goto 20130914  "SEQ_NUM"
		Mobile_HTML5Function.Func_seq_num_initialization(html_env.getGlevel());
	}

	public static void G1(Mobile_HTML5G1 G1, Mobile_HTML5Env html_env, ExtList data_info, ExtList data, ITFE tfe){
		String classid = Mobile_HTML5Env.getClassID(G1);
		String classid2 = Mobile_HTML5Env.getClassID(tfe);
		String row = "";
		String column = "";
		String[] ifs_div_String = {"", ""};
		String classid_for_ifs = "";
		codes = new StringBuffer();
		StringBuffer tmp = new StringBuffer();
		
		if(G1.decos.containsKey("infinite-scroll")){
			classid_for_ifs = classid + "_wrapper";
			DecorateList deco_ifs = new DecorateList();
			deco_ifs.put("infinite-scroll", "");
			html_env.append_css_def_td(G1.getSymbol(), classid_for_ifs, classid2, deco_ifs);
			G1.decos.remove("infinite-scroll");
			ifs_div_String = Mobile_HTML5.ifs_div_start(G1.getSymbol(), html_env, classid_for_ifs, ifs_div_String);
		}
		html_env.append_css_def_td(classid, G1.decos);
		// ページネーション
		if (G1.decos.containsKey("row") && G1.decos.containsKey("column")) {
			html_env.g1PaginationRowNum = Integer.parseInt(G1.decos
					.getStr("row"));
			row = " row=\'" + html_env.g1PaginationRowNum + "\'";
			html_env.g1PaginationColumnNum = Integer.parseInt(G1.decos
					.getStr("column"));
			column = " column=\'" + html_env.g1PaginationColumnNum + "\'";
		} else if (G1.decos.containsKey("column")) { // 複合反復子
			html_env.g1RetNum = Integer.parseInt(G1.decos.getStr("column"));
			column = " column=\'" + html_env.g1RetNum + "\'";
		}

		if(!GlobalEnv.isOpt()){
			if(!Sass.isBootstrapFlg()){
				//20130503  Panel
				//				G1.panelFlg = Mobile_HTML5C1.panelProcess1(G1.decos, html_env);

				//20130330 tab
				//tab1
				if(G1.decos.containsKey("tab1")){
					html_env.code.append("<div data-role=\"content\"> <div id=\"tabs\">\n<ul>\n");
					html_env.code.append("	<li><a href=\"#tabs-"+Mobile_HTML5Env.tabCount+"\">");
					
					tmp.append("<div data-role=\"content\"> <div id=\"tabs\">\n<ul>\n");
					tmp.append("	<li><a href=\"#tabs-"+Mobile_HTML5Env.tabCount+"\">");
					if(!G1.decos.getStr("tab1").equals("")){
						html_env.code.append(G1.decos.getStr("tab1"));
						tmp.append(G1.decos.getStr("tab1"));
					}
					else{
						html_env.code.append("tab1");
						tmp.append("tab1");
					}
					html_env.code.append("</a></li>\n");
					html_env.code.append("</ul>\n<div id=\"tabs-"+Mobile_HTML5Env.tabCount+"\">\n");
					
					tmp.append("</a></li>\n");
					tmp.append("</ul>\n<div id=\"tabs-"+Mobile_HTML5Env.tabCount+"\">\n");
				}
				//tab2〜tab15
				else{
					int i=2;
					while(i<=Mobile_HTML5Env.maxTab){		//HTMLEnv.maxTab=15
						if(G1.decos.containsKey("tab"+i) || (i==2 && G1.decos.containsKey("tab"))){
							//replace: </ul>の前に<li>〜</li>を付加
							String a = "</ul>";
							String b = "	<li><a href=\"#tabs-"+Mobile_HTML5Env.tabCount+"\">";
							if(G1.decos.containsKey("tab"+i))
								if(!G1.decos.getStr("tab"+i).equals(""))	b += G1.decos.getStr("tab"+i);
								else				            		b += "tab"+i;
							else
								if(!G1.decos.getStr("tab").equals(""))		b += G1.decos.getStr("tab");
								else				            		b += "tab";
							b += "</a></li>\n";
							Mobile_HTML5Manager.replaceCode(html_env, a, b+a);

							//replace: 最後の</div></div></div>カット
							Mobile_HTML5Manager.replaceCode(html_env, "</div></div></div>", "");

							//replace: 不要な「<div class=〜」をカット
							Mobile_HTML5Manager.replaceCode(html_env, "<div class=\""+classid+" \">", "");

							html_env.code.append("<div id=\"tabs-"+Mobile_HTML5Env.tabCount+"\">\n");
							tmp.append("<div id=\"tabs-"+Mobile_HTML5Env.tabCount+"\">\n");
							break;
						}
						i++;
					}
				}

				//20130312 collapsible
				if(G1.decos.containsKey("collapse")){
					html_env.code.append("<DIV data-role=\"collapsible\" data-content-theme=\"c\" style=\"padding: 0px 12px;\">\n");
					tmp.append("<DIV data-role=\"collapsible\" data-content-theme=\"c\" style=\"padding: 0px 12px;\">\n");

					//header
					if(!G1.decos.getStr("collapse").equals("")){
						html_env.code.append("	<h1>"+G1.decos.getStr("collapse")+"</h1>\n");
						tmp.append("	<h1>"+G1.decos.getStr("collapse")+"</h1>\n");
					}else{
						html_env.code.append("<h1>Contents</h1>\n");
						tmp.append("<h1>Contents</h1>\n");
					}
				}
				//20130309
				if(!G1.tableFlg){
					//        		if(html_env.written_classid.contains(classid))
					html_env.code.append("\n<DIV Class=\"ui-grid ##"+Mobile_HTML5Env.uiGridCount2+" "+classid+"\"");
					tmp.append("\n<DIV Class=\"ui-grid ##"+Mobile_HTML5Env.uiGridCount2+" "+classid+"\"");
					//        		else
					//        			html_env.code.append("\n<DIV Class=\"ui-grid ##"+Mobile_HTML5Env.uiGridCount2+"\"");
					html_env.code.append(">\n");
					tmp.append(">\n");
					Mobile_HTML5Env.uiGridCount2++;
				}

				//20130314  table
				if(G1.tableFlg){
					//added by goto 20130318  横スクロール
					if(G1.numberOfColumns < 0){
						html_env.code.append("<div style=\"overflow:auto;\">\n");	//20130917  [ ],10@{table}
						tmp.append("<div style=\"overflow:auto;\">\n");	//20130917  [ ],10@{table}
					}

					if(G1.row>1 && G1.tableFlg)	Mobile_HTML5G2.tableStartTag = Mobile_HTML5C1.getTableStartTag(html_env, G1.decos, G1)+"<TR>";
					else{
						html_env.code.append(Mobile_HTML5C1.getTableStartTag(html_env, G1.decos, G1)+"<TR>");
						tmp.append(Mobile_HTML5C1.getTableStartTag(html_env, G1.decos, G1)+"<TR>");
					}
				}
			}else if(Sass.isBootstrapFlg()){
				//        		if(!decos.containsKey("C1") && !decos.containsKey("G1")){
				//            		html_env.code.append("<DIV Class=\"row\">");
				//            		if(Sass.outofloopFlg.peekFirst()){
				//            			Sass.makeRowClass();
				//            		}
				//            	}
				//        		
				//        		html_env.code.append("<DIV Class=\""+classid+"\">");
				//        		html_env.code.append("<DIV Class=\"row\">");
				//        		if(Sass.outofloopFlg.peekFirst()){
				//        			Sass.makeClass(classid);
				//        			Sass.defineGridBasic(classid, decos);
				//        			Sass.makeRowClass();
				//	      		}
				//        		Sass.beforeLoop();

				if(G1.firstFlg){
					html_env.code.append("<DIV Class=\"row\">\n");
					html_env.code.append("<DIV Class=\""+classid+"\">\n");
					
					tmp.append("<DIV Class=\"row\">\n");
					tmp.append("<DIV Class=\""+classid+"\">\n");
					if(Sass.outofloopFlg.peekFirst()){        				
						//        				Sass.makeClass(classid);
						//        				Sass.defineGridBasic(classid, decos);
						//        				Sass.closeBracket();
						Sass.makeColumn(classid, G1.decos, "", -1);
					}
				}

				html_env.code.append("<DIV Class=\"row\">\n");
				tmp.append("<DIV Class=\"row\">\n");
				if(Sass.outofloopFlg.peekFirst()){
					//        			Sass.makeRowClass();
				}

				Sass.beforeLoop();

				//				if(decos.containsKey("slide")){
				//					html_env.code.append("<div id=\"carousel-example-generic\" class=\"carousel slide\" data-ride=\"carousel\">\n"
				//							+ "<!-- Indicators -->\n"
				//							+ "<ol class=\"carousel-indicators\">\n");
				//					for(int i = 0; i < this.numberOfColumns; i++){
				//						if(i == 0){
				//							html_env.code.append("<li data-target=\"#carousel-example-generic\" data-slide-to=\""+ i +"\" class=\"active\"></li>\n");
				//						}else{
				//							html_env.code.append("<li data-target=\"#carousel-example-generic\" data-slide-to=\""+ i +"\"></li>\n");
				//						}
				//					}
				//					html_env.code.append("</ol>\n"
				//							+ "<!-- Wrapper for slides -->\n"
				//							+ "<div class=\"carousel-inner\" role=\"listbox\">\n");
				//				}
				//added 161125 taji for @slide	
			}
		}
		// System.out.println("G1 tableFlg = " + tableFlg + ", divFlg = " +
		// divFlg);
		html_env.append_css_def_td(HTMLEnv.getClassID(G1), G1.decos);
		Mobile_HTML5.beforeWhileProcess(G1.getSymbol(), G1.decos, html_env, ifs_div_String);
		Infinite.beforeWhileProcess(G1.getSymbol(), G1.decos, html_env, ifs_div_String, classid_for_ifs, tmp);
		while (G1.hasMoreItems()) {
			Mobile_HTML5.gLevel1++;
			Mobile_HTML5.whileProcess1_1(G1.getSymbol(), G1.decos, html_env, data, data_info, tfe, null, -1);

			Mobile_HTML5Function.setGlvl(html_env.getGlevel());	//added by goto 20130914  "SEQ_NUM"
			if(G1.decos.containsKey("table0") || Mobile_HTML5C1.table0Flg || Mobile_HTML5C2.table0Flg || Mobile_HTML5G2.table0Flg)	G1.table0Flg = true;
			if(G1.decos.containsKey("table") || Mobile_HTML5C1.tableFlg || Mobile_HTML5C2.tableFlg || Mobile_HTML5G2.tableFlg || G1.table0Flg)	G1.tableFlg=true;
			if(G1.decos.containsKey("div")){
				G1.divFlg = true;
				G1.tableFlg = false;
			}
			html_env.setGlevel(html_env.getGlevel() + 1);

			if(G1.rowFlg){
				html_env.code = new StringBuffer();
				html_env.countfile++;
				html_env.filename = html_env.outfile + "_row" + Mobile_HTML5G2.rowFileNum + "_" + G1.j + Compiler.getExtension();
				html_env.nextbackfile = html_env.linkoutfile + "_row" + Mobile_HTML5G2.rowFileNum + "_" + G1.j + Compiler.getExtension();
				html_env.setOutlineMode();
			}


			//20130309
			G1.Count = ( ((G1.gridInt>=G1.jj)&&(Mobile_HTML5G1.G1Flg))? G1.jj:G1.gridInt );
			G1.Count %= G1.numberOfColumns;


			//20130917  [ ],10@{table}
			if(G1.tableFlg && G1.numberOfColumns > 1 && G1.Count == 0){
				if(G1.table_column_num>0)	html_env.code.append("</TR><TR>\n");
				else					G1.table_column_num++;
			}

			if(!Sass.isBootstrapFlg()){
				//20130309
				if(!G1.tableFlg){
					//20131002
					//TODO style=を.cssへ
					String divWidth = Mobile_HTML5.getDivWidth("G1", G1.decos, G1.numberOfColumns - Mobile_HTML5Function.func_null_count);	//null()
					if(!G1.decos.containsKey("column")){
						if(G1.Count!=0){
							html_env.code.append("\n<div class=\"ui-block"+" "+classid2+" "+classid2+"-"+G1.G1_count+"\">\n");
							tmp.append("\n<div class=\"ui-block"+" "+classid2+" "+classid2+"-"+G1.G1_count+"\">\n");
						}
						else{
							if(!Mobile_HTML5_dynamic.dynamicDisplay){
								html_env.code.append("\n<div class=\"ui-block"+" "+classid2+" "+classid2+"-"+G1.G1_count+"\" style=\"clear:left;\">\n");
								tmp.append("\n<div class=\"ui-block"+" "+classid2+" "+classid2+"-"+G1.G1_count+"\" style=\"clear:left;\">\n");
							}else{
								//html_env.code.append("\n<div class=\"ui-block"+" "+classid2+" "+classid2+"-"+G1_count+"\" '.(($j++>0)? '' : 'style=\"clear:left;\"').'>\n");
								html_env.code.append("\n<div class=\"ui-block"+" "+classid2+" "+classid2+"-"+G1.G1_count+"\" '.(($i"+(Mobile_HTML5.gLevel0+1)+">0)? '' : 'style=\"clear:left;\"').'>\n");	//TODO d2 change if ?
								tmp.append("\n<div class=\"ui-block"+" "+classid2+" "+classid2+"-"+G1.G1_count+"\" '.(($i"+(Mobile_HTML5.gLevel0+1)+">0)? '' : 'style=\"clear:left;\"').'>\n");
							}
						}
						if(G1.gridInt<1)	Mobile_HTML5Attribute.attributeDivWidth2 += "."+classid2+"-"+G1.G1_count+"{ width:"+divWidth+"; }\n";
					}else{
						if(G1.Count!=0){
							html_env.code.append("\n<div class=\"ui-block"+" "+classid2+"\">\n");
							tmp.append("\n<div class=\"ui-block"+" "+classid2+"\">\n");
						}
						else{
							html_env.code.append("\n<div class=\"ui-block"+" "+classid2+"\" style=\"clear:left;\">\n");
							tmp.append("\n<div class=\"ui-block"+" "+classid2+"\" style=\"clear:left;\">\n");
						}
						Mobile_HTML5Attribute.attributeDivWidth = divWidth;
					}
					//System.out.println(this.decos+ " "+ divWidth+" "+gridInt);
				}
				//20130314  table
				//20160527 bootstrap
				else if(G1.tableFlg){
					html_env.code.append("<TD class=\"" + classid2 + " nest\">\n");
					tmp.append("<TD class=\"" + classid2 + " nest\">\n");
				}

				classid = classid2;

				//    	    if(Mobile_HTML5Env.dynamicFlg){	//20130529 dynamic
				//	      		//☆★
				//	      		Log.info("★★G1-1 tfe : " + tfe);
				//	    		//☆★            Log.info("G1 tfe : " + tfe);
				//	            //☆★            Log.info("G1 tfes : " + this.tfes);
				//	            //☆★            Log.info("G1 tfeItems : " + this.tfeItems);
				//	      	}
			}else if(Sass.isBootstrapFlg()){
				G1.decos.put("G1",""+(G1.numberOfColumns - Mobile_HTML5Function.func_null_count));
				html_env.code.append("<div class=\"" + classid2 +"\">\n");
				tmp.append("<div class=\"" + classid2 +"\">\n");
				if(Sass.outofloopFlg.peekFirst()){
					//            		Sass.makeClass(classid2);
					//            		Sass.defineGridBasic(classid2, (tfe).decos);

					//            		Sass.makeClass(classid2);
					//            		Sass.defineGridBasic(classid2, (tfe).decos);
					//            		Sass.closeBracket();
					//taji comment out
					//					Sass.makeColumn(classid2, (tfe).decos, getSymbol(), responsiveId);
					//				}
				}
			}

			Mobile_HTML5.whileProcess1_2(G1.getSymbol(), G1.decos, html_env, data, data_info, tfe, null, -1);

			G1.worknextItem();
			tmp.append(codes.toString());
			Mobile_HTML5.whileProcess2_1(G1.getSymbol(), G1.decos, html_env, data, data_info, tfe, null, -1);

			if(G1.decos.containsKey("table0") || Mobile_HTML5C1.table0Flg || Mobile_HTML5C2.table0Flg || Mobile_HTML5G2.table0Flg)	Mobile_HTML5G1.table0Flg = true;
			if(G1.decos.containsKey("table") || Mobile_HTML5C1.tableFlg || Mobile_HTML5C2.tableFlg || Mobile_HTML5G2.tableFlg || Mobile_HTML5G1.table0Flg)	Mobile_HTML5G1.tableFlg=true;
			if(G1.decos.containsKey("div")){
				G1.divFlg = true;
				G1.tableFlg = false;
			}

			G1.ii++;
			G1.jj++;
			G1.gridInt++;

			//20160527 bootstrap
			if(!Sass.isBootstrapFlg()){
				if(!G1.tableFlg){
					html_env.code.append("</div>");	//20130309
					tmp.append("</div>");
				}
				else if(G1.tableFlg){
					html_env.code.append("</TD>\n");    //20130314 table
					tmp.append("</TD>\n");    //20130314 table
				}
			}else if(Sass.isBootstrapFlg()){
				html_env.code.append("</div>\n");//classid2
				tmp.append("</div>\n");//classid2
				if(Sass.outofloopFlg.peekFirst()){
					//            		Sass.closeBracket();//classid2
				}
				Sass.afterFirstLoop();
			}

			html_env.setGlevel(html_env.getGlevel() - 1);

			Mobile_HTML5G2.tableDivHeader_Count1++;	//20131001 tableDivHeader

			//added by goto 20130413  "row Prev/Next"
			if(G1.rowFlg){
				G1.codeBuf.append(html_env.code);
				if((G1.rowNum+1)%(G1.row*G1.numberOfColumns)==0){
					Mobile_HTML5G2.createHTMLfile_ForPrevNext(html_env, G1.codeBuf);
					G1.j++;
					G1.codeBuf = new StringBuffer();
				}
				G1.rowNum++;
			}

			if(!Mobile_HTML5.whileProcess2_2(G1.getSymbol(), G1.decos, html_env, data, data_info, tfe, null, -1))	break;
			Mobile_HTML5.gLevel1--;
		}
		Infinite.afterWhileProcess(G1.getSymbol(), classid2, G1.decos, html_env, ifs_div_String, classid_for_ifs, tmp);
		Mobile_HTML5.afterWhileProcess(G1.getSymbol(), classid2, G1.decos, html_env, ifs_div_String);

		//added by goto 20130413  "row Prev/Next"
		if(G1.rowFlg){
			if(G1.rowNum%(G1.row*G1.numberOfColumns)!=0){	//最後の child HTML を create
				Mobile_HTML5G2.createHTMLfile_ForPrevNext(html_env, G1.codeBuf);
			}
			//ファイル名・コード等をparent HTMLのものへ戻す
			//					html_env.filename = parentfile;
			//					html_env.code = parentcode;
			//					html_env.header = parentheader;
			//					html_env.footer = parentfooter;
			//					html_env.nextbackfile = parentnextbackfile;
			Log.out("TFEId = " + classid);
			html_env.append_css_def_td(classid, G1.decos);

			int first = 1, last = ((G1.rowNum%(G1.row*G1.numberOfColumns)!=0)? (G1.rowNum/(G1.row*G1.numberOfColumns)+1):(G1.rowNum/(G1.row*G1.numberOfColumns)));	//for G1
			Mobile_HTML5G2.PrevNextProcess(html_env, G1.rowNum, G1.row, first, last, G1.numberOfColumns);
		}

		/* 
//	    		//,で結合(水平結合)した際
//	    		//replace: 不要な「<div class=〜」をカット
//				String[] s = {"a","b","c","d","e"};
//				int j=0;
//				while(!HTMLManager.replaceCode(html_env, "<div class=\"ui-block-"+s[j]+" "+HTMLEnv.getClassID(this)+"\">", "")){
//					j++;
//					if(j>4) break;
//				}
		 */

		if(Mobile_HTML5Env.getFormItemFlg()){		
			Mobile_HTML5Env.incrementFormPartsNumber();
		}

		if(!Sass.isBootstrapFlg()){
			if(!G1.tableFlg){
				html_env.code.append("\n</DIV>\n");			//20130309	//20160527 bootstrap
				tmp.append("\n</DIV>\n");
			}
			else if (G1.tableFlg){	//20160527 bootstrap
				if(!(G1.row>1 && G1.tableFlg)){
					html_env.code.append("</TR></TABLE>\n");	//20130314  table
					tmp.append("</TR></TABLE>\n");	//20130314  table
				}
				G1.tableFlg = false;
				G1.table0Flg = false;		//20130325 table0
				if(G1.numberOfColumns < 0){
					html_env.code.append("</div>\n");	//added by goto 20130318  横スクロール		//20130917  [ ],10@{table}
					tmp.append("</div>\n");
				}
			}
		}else if(Sass.isBootstrapFlg()){
			if(G1.decos.containsKey("slide")){
				html_env.code.append("</div>\n");
				html_env.code.append("<!-- Controls -->\n"
						+ "<a class=\"left carousel-control\" href=\"#carousel-example-generic\" role=\"button\" data-slide=\"prev\">\n"
						+ "<span class=\"glyphicon glyphicon-chevron-left\" aria-hidden=\"true\"></span>\n"
						+ "<span class=\"sr-only\">Previous</span>\n"
						+ "</a>\n"
						+ "<a class=\"right carousel-control\" href=\"#carousel-example-generic\" role=\"button\" data-slide=\"next\">\n"
						+ "<span class=\"glyphicon glyphicon-chevron-right\" aria-hidden=\"true\"></span>\n"
						+ "<span class=\"sr-only\">Next</span>\n"
						+ "</a>\n"
						+ "</div>\n");
				
				tmp.append("</div>\n");
				tmp.append("<!-- Controls -->\n"
						+ "<a class=\"left carousel-control\" href=\"#carousel-example-generic\" role=\"button\" data-slide=\"prev\">\n"
						+ "<span class=\"glyphicon glyphicon-chevron-left\" aria-hidden=\"true\"></span>\n"
						+ "<span class=\"sr-only\">Previous</span>\n"
						+ "</a>\n"
						+ "<a class=\"right carousel-control\" href=\"#carousel-example-generic\" role=\"button\" data-slide=\"next\">\n"
						+ "<span class=\"glyphicon glyphicon-chevron-right\" aria-hidden=\"true\"></span>\n"
						+ "<span class=\"sr-only\">Next</span>\n"
						+ "</a>\n"
						+ "</div>\n");
			}
			//        	html_env.code.append("\n</DIV>\n");//.row
			//        	html_env.code.append("\n</DIV>\n");//.TFE
			//      		if(Sass.outofloopFlg.peekFirst()){
			//      			Sass.closeBracket();
			//      			Sass.closeBracket();
			//      		}
			//      		if(!decos.containsKey("C1") && !decos.containsKey("G1")){
			//        		html_env.code.append("\n</DIV>\n");
			//        		if(Sass.outofloopFlg.peekFirst()){
			//        			Sass.closeBracket();
			//        		}
			//        	}

			html_env.code.append("</DIV>\n");//.row
			tmp.append("</DIV>\n");//.row
			if(Sass.outofloopFlg.peekFirst()){
				//        		Sass.closeBracket();//row
			}

			if(G1.firstFlg){
				html_env.code.append("</DIV>\n");//.classid
				html_env.code.append("</DIV>\n");//.row
				
				tmp.append("</DIV>\n");//.classid
				tmp.append("</DIV>\n");//.row
				if(Sass.outofloopFlg.peekFirst()){
					//        			Sass.closeBracket();//classid
					//        			Sass.closeBracket();//row
				}
				G1.firstFlg = false;
			}
		}
		if(G1.divFlg)	G1.divFlg = false;		//20130326  div

		G1.G1Flg=false;
		Log.out("</TR></TABLE>");

		//20130312 collapsible
		if(G1.decos.containsKey("collapse")){
			html_env.code.append("</DIV>");
			tmp.append("</DIV>");
		}

		//20130330 tab
		int a=1;
		while(a<=Mobile_HTML5Env.maxTab){
			if(G1.decos.containsKey("tab"+a) || (a==1 && G1.decos.containsKey("tab"))){
				html_env.code.append("</div></div></div>\n");
				tmp.append("</div></div></div>\n");
				Mobile_HTML5Env.tabCount++;
				break;
			}
			a++;
		}

		//20130503  Panel
		//				Mobile_HTML5C1.panelProcess2(G1.decos, html_env, G1.panelFlg);

		//added by goto 20130413  "row Prev/Next"
		if(G1.rowFlg){
			Mobile_HTML5G2.rowFileNum++;
			G1.rowFlg = false;
			Mobile_HTML5G2.tableStartTag = "";
		}
		Mobile_HTML5.postProcess(G1.getSymbol(), classid2, G1.decos, html_env);	//Post-process (後処理)
		codes.append(tmp.toString());
	}


	public static void setWidth(String classid, DecorateList decos, Mobile_HTML5Env html_env) {
		if(!decos.containsKey("width") && (!Mobile_HTML5Attribute.attributeDivWidth.equals("") || !Mobile_HTML5Attribute.attributeDivWidth2.equals(""))){
			//attributeDivWidth, attributeDivWidth2
			if(!Mobile_HTML5Attribute.attributeDivWidth2.equals("") && !Mobile_HTML5Attribute.attributeHasWidth){
				html_env.css.append(Mobile_HTML5Attribute.attributeDivWidth2);
			}else if(!Mobile_HTML5Attribute.attributeDivWidth.equals("")){
				decos.put("width", Mobile_HTML5Attribute.attributeDivWidth);
			}
			Mobile_HTML5Attribute.attributeDivWidth2 = "";
		}
		Mobile_HTML5Attribute.attributeDivWidth = "";
		if(decos.containsKey("width"))	Mobile_HTML5Attribute.attributeHasWidth = true;
		else							Mobile_HTML5Attribute.attributeHasWidth = false;
		html_env.append_css_def_td(classid, decos);
	}
	private static void createForm(ExtList data_info, Mobile_HTML5Attribute ATT, Mobile_HTML5Env html_env, Mobile_HTML5Env html_env2, StringBuffer tmp){

		String form = new String();
		String name = new String();		
		String inputFormString = new String();

		for(int i = 1; i < formSql.length ; i++ ){
			if(ATT.decos.containsKey(formSql[i]) || Mobile_HTML5Env.getIDU().equals(formSql[i])){
				switch(i){
				case 1 : //delete
					if(ATT.decos.containsKey(formSql[i])){
						name = ATT.decos.getStr("delete");
					}else{
						name = ATT.decos.getStr("attributeName");
					}
					inputFormString += "<input type=\"checkbox\" name=\"" + name + "\" value=\"" + ATT.getStr(data_info) + "\" />";
					whichForm = i;
					break;
				case 2 : //update
					if(ATT.decos.containsKey(formSql[i])){
						name = ATT.decos.getStr("update");
					}else{
						name = ATT.decos.getStr("attributeName");
					}
					whichForm = i;
					break;
				case 3 : //insert
					if(ATT.decos.containsKey(formSql[i])){
						name = ATT.decos.getStr("insert");
					}else{
						name = ATT.decos.getStr("attributeName");
					}
					whichForm = i;
					break;
				case 4 : //login
					name = ATT.decos.getStr("login");
					if(ATT.decos.containsKey("att")){
						inputFormString += "<input type=\"hidden\" name=\"att\" value=\"" + ATT.decos.getStr("att") +"\" />";
					}
					whichForm = i;
					break;
				case 5 : //logout
					inputFormString += "<input type=\"hidden\" name=\"sqlfile\" value=\""+ATT.decos.getStr("linkfile").replace("\"", "")+"\" />";
					inputFormString += "<input type=\"submit\" name=\"logout\" value=\""+ATT.getStr(data_info)+"\" />";
					whichForm = i;
					break;
				}	
			}
		}


		if( 1 < whichForm && whichForm < formSql.length-1 ){ //update,insert,login
			String s;
			if(whichForm < 3) {//update
				s = ATT.getStr(data_info);
			}else{//insert,login,logout
				s = "";
			}
			if(ATT.decos.containsKey("pwd")){
				inputFormString += "<input type=\"password\" name=\"" + name + "\" value=\"" + s + "\" />";
				if(ATT.decos.containsKey("md5")){
					inputFormString += "<input type=\"hidden\" name=\"" + name + ":pwd\" value=\"md5\" />";
				}

			}else{
				if(s.isEmpty()){
					inputFormString += "<input type=\"text\" name=\"" + name + "\" />";					
				}else{
					inputFormString += "<input type=\"text\" name=\"" + name + "\" value=\"" + s + "\" />";
				}
			}	

			//add constraint
			String constraint = new String();
			if(ATT.decos.containsKey("notnull")){//not null
				constraint = "notnull";
			}
			if(ATT.decos.containsKey("number")){//num or eng
				if(ATT.decos.containsKey("english")){
					if(constraint.isEmpty())
						constraint = "numeng";
					else
						constraint += ",numeng";
				}else{//number
					if(constraint.isEmpty())
						constraint = "number";
					else
						constraint += ",number";
				}	
			}else if(ATT.decos.containsKey("english")){//eng
				if(constraint.isEmpty())
					constraint = "english";
				else
					constraint += ",english";
			}

			if(ATT.decos.containsKey("unique")){//unique
				if(constraint.isEmpty())
					constraint = "unique";
				else
					constraint += ",unique";
			}

			if(constraint != null && !constraint.isEmpty())
				inputFormString += "<input type=\"hidden\" name=\""+ name +":const\" value=\""+ constraint +"\" />";


			Log.out("pppppp"+ATT.decos.containsKey("pkey"));
			if(ATT.decos.containsKey("pkey") && whichForm == 2){//update
				if(!html_env.code.toString().contains("<input type=\"hidden\" name=\"pkey\" value=\"" + name + "\" />"))
					inputFormString += "<input type=\"hidden\" name=\"pkey\" value=\"" + name + "\" />";
			}
		}

		html_env.code.append(inputFormString);
		tmp.append(inputFormString);
		html_env2.code.append(inputFormString);
		Log.out(inputFormString);

		inputFormString = new String();

		if(Mobile_HTML5Env.getFormItemFlg()){
			for(int i = 1; i < formHtml.length ; i++ ){
				String real_value = Mobile_HTML5Env.getFormValueString();
				if(Mobile_HTML5Env.getFormItemName().equals(formHtml[i])){
					switch(i){
					case 1: //submit
						inputFormString = inputFormItems(data_info,formHtml[i],"", ATT);
						whichForm =  i + formSql.length;
						break;
					case 2: //select
						inputFormString = inputFormItems(data_info,formHtml[i],real_value, ATT);
						whichForm =  i + formSql.length;
						break;
					case 3: //checkbox
						inputFormString = inputFormItems(data_info,formHtml[i],real_value, ATT);
						whichForm =  i + formSql.length;
						break;
					case 4: //radio
						inputFormString = inputFormItems(data_info,formHtml[i],real_value, ATT);
						whichForm =  i + formSql.length;
						break;

					case 5: //input text
						inputFormString = inputFormItems(data_info,formHtml[i],real_value, ATT);
						whichForm =  i + formSql.length;
						break;

					case 6: //textarea
						inputFormString = inputFormItems(data_info,formHtml[i],real_value, ATT);
						whichForm =  i + formSql.length;
						break;

					case 7: //hidden
						inputFormString = inputFormItems(data_info,formHtml[i],real_value, ATT);
						whichForm =  i + formSql.length;
						break;
					}
				}
			}
		}

		html_env.code.append(inputFormString);
		tmp.append(inputFormString);
		html_env2.code.append(inputFormString);
		Log.out(inputFormString);

	}

	private static String inputFormItems(ExtList data_info,String itemType,String real_value, Mobile_HTML5Attribute ATT){
		String ret = "";
		String formname = Mobile_HTML5Env.getFormPartsName();;
		if(Mobile_HTML5Env.getSearch()){
			ret += cond();
			formname = "value"+Mobile_HTML5Env.form_parts_number;
		}
		String s = ATT.getStr(data_info);
		//tuple_count++;
		if(real_value.isEmpty()){
			real_value = s;
		}
		//sizeoption
		String size = new String();
		if(ATT.decos.containsKey("size")){
			size += " size=\""+ ATT.decos.getStr("size")+"\"";
		}
		if(ATT.decos.containsKey("height")){
			size += " height=\""+ ATT.decos.getStr("height")+"\"";
		}
		if(ATT.decos.containsKey("cols")){
			size += " cols=\""+ ATT.decos.getStr("cols")+"\"";
		}
		if(ATT.decos.containsKey("rows")){
			size += " rows=\""+ ATT.decos.getStr("rows")+"\"";
		}

		if(ATT.decos.containsKey("class")){
			size += " class=\""+ ATT.decos.getStr("class")+"\"";
		}

		if(itemType.equals(formHtml[1])){//submit

		}else if(itemType.equals(formHtml[2])){//select
			if(Mobile_HTML5Env.getSelectRepeat() == false){
				ret += "<select name=\""+ formname +"\">";
				Mobile_HTML5Env.setSelectRepeat(true);
			}
			if(Mobile_HTML5Env.getSelected().length() != 0 && Mobile_HTML5Env.getSelected().equals(real_value)){
				ret += "<option value=\"" + real_value + "\"" + size +" selected=\"selected\" >" + s + "</option>";
			}else{
				ret += "<option value=\"" + real_value + "\"" + size +" >" + s + "</option>";
			}
		}else if(itemType.equals(formHtml[3])){//checkbox
			String checked = "";
			if(Mobile_HTML5Env.getChecked().length() != 0 && Mobile_HTML5Env.getChecked().equals(real_value)){
				checked = " checked=\"checked\" ";
			}
			if(Mobile_HTML5Env.nameId.length() != 0){
				ret += "<input type=\"checkbox\" name=\""+ formname + "[" + Mobile_HTML5Env.nameId+ "]" + "\" value=\"" + real_value + "\"" + size + checked +" />";
				ret += s;
			}else{
				ret += "<input type=\"checkbox\" name=\""+ formname +"\" value=\"" + real_value + "\"" + size + checked +" />";
				ret += s;
			}
		}else if(itemType.equals(formHtml[4])){//radio
			String checked = "";
			if(Mobile_HTML5Env.getChecked().length() != 0 && Mobile_HTML5Env.getChecked().equals(real_value)){
				checked = " checked=\"checked\" ";
			}
			ret += "<input type=\"radio\" name=\""+ formname +"\" value=\"" + real_value + "\"" + size + checked + " />";
			ret += s;
		}else if(itemType.equals(formHtml[5])){//text
			if(ATT.decos.containsKey("pwd")){
				ret += "<input type=\"password\" name=\""+ formname +"\" value=\"" + real_value + "\"" + size +" />";
				if(ATT.decos.containsKey("md5")){
					ret += "<input type=\"hidden\" name=\"" + formname + ":pwd\" value=\"md5\" />";
				}
			}else{
				ret += "<input type=\"text\" name=\""+ formname +"\" value=\"" + real_value + "\"" + size +" />";
			}
		}else if(itemType.equals(formHtml[6])){//textarea
			ret += "<textarea name=\""+ formname + "\"" + size +">";
			if(s != null){
				ret += s;
			}
			ret += "</textarea>";
		}else if(itemType.equals(formHtml[7])){//text
			ret += "<input type=\"hidden\" name=\""+ formname +"\" value=\"" + real_value + "\"" + size +" />";
		}

		String constraint = new String();
		if(ATT.decos.containsKey("notnull")){
			constraint = "notnull";
		}
		if(ATT.decos.containsKey("number")){
			if(ATT.decos.containsKey("english")){//number or english
				if(constraint.isEmpty())
					constraint = "numeng";
				else
					constraint += ",numeng";
			}else{//number
				if(constraint.isEmpty())
					constraint = "number";
				else
					constraint += ",number";
			}
		}else if(ATT.decos.containsKey("english")){//english
			if(constraint.isEmpty())
				constraint = "english";
			else
				constraint += ",english";
		}
		if(ATT.decos.containsKey("unique")){//unique
			if(constraint.isEmpty())
				constraint = "unique";
			else
				constraint += ",unique";
		}

		if(constraint != null && !constraint.isEmpty())
			ret += "<input type=\"hidden\" name=\""+ formname +":const\" value=\""+ constraint +"\" />";


		return ret;
	}

	private static String cond(){
		String ret = "";
		if(Mobile_HTML5Env.form_parts_number != Mobile_HTML5Env.searchid){
			Mobile_HTML5Env.searchid = Mobile_HTML5Env.form_parts_number;
			if(!Mobile_HTML5Env.cond_name.isEmpty() && !Mobile_HTML5Env.cond.isEmpty()){
				ret += "<input type=\"hidden\" name=\"cond_name"+ Mobile_HTML5Env.form_parts_number +"\" value=\""+ Mobile_HTML5Env.cond_name +"\" />";
				ret += "<input type=\"hidden\" name=\"cond"+ Mobile_HTML5Env.form_parts_number +"\" value=\""+ Mobile_HTML5Env.cond +"\" />";
				ret += "<input type=\"hidden\" name=\"value_type"+ Mobile_HTML5Env.form_parts_number +"\" value=\"String\" />";
			}
		}
		return ret;
	}
}
