package com.xmiles.android.support;

import java.util.List;


import android.content.Context;
import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;


public class GetUberPackageInfo {
	


	private PackageInfo pinfo_uber = null;
	private String uber_package;
	private Context _context;
	
	public GetUberPackageInfo(Context context){
		this._context = context;
		
	}
	
	public String getUberPackageInfo(){
		
		
		try {
			this.pinfo_uber = _context.getPackageManager().getPackageInfo("com.ubercab", 0);
			this.uber_package = this.pinfo_uber.packageName;

			
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.uber_package = "NO";
		}
		
				
		return uber_package;
		
		
		
	}

}
