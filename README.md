REST.Android
============

MercuryPay RESTful sample project that will show you how to process againsts PaymentsAPI.

Contact your Mercury Developer Integration Analyst for the transactions your POS environment must support. 

##Step 1: Build Request with Key Value Pairs
  
Create a HashMap and add all the Key Value Pairs.
  
```Java
Map<String, String> map = new HashMap<String, String>();
map.put("InvoiceNo","2001");
map.put("RefNo","2001");
map.put("Memo","REST.android");
map.put("Purchase","2.00");
map.put("LaneID", "02");
map.put("Frequency","OneTime");
map.put("RecordNo","RecordNumberRequested");
map.put("EncryptedFormat","MagneSafe");
map.put("AccountSource","Swiped");
map.put("EncryptedBlock","2F8248964608156B2B1745287B44CA90A349905F905514ABE3979D7957F13804705684B1C9D5641C");
map.put("EncryptedKey","9500030000040C200026");
```
  
##Step 2: Process the Transaction

Process the transaction with JSON POST.

```Java
String line;
String response = "";
		
try {
	String jsonString = new JSONObject(map).toString();
	URL url = new URL("https://w1.mercurycert.net/PaymentsAPI/Credit/Sale");
	HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
	conn.setRequestMethod("POST");
	conn.setRequestProperty("Authorization", auth);
	conn.setRequestProperty("Content-Type", "application/json");
	conn.setRequestProperty("Content-Length", Integer.toString(jsonRequest.getBytes().length));
	conn.setConnectTimeout(10000);
	conn.setReadTimeout(30000);
	conn.setDoInput(true);
	conn.setDoOutput(true);
	OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
	writer.write(jsonRequest);
	writer.close();

	if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

		while ((line = rd.readLine()) != null) {
			response += line;
		}

		rd.close();	
	}

	conn.disconnect();
}
catch (Exception e) {
	e.printStackTrace();
}
```

##Step 3: Parse the Response

Approved transactions will have a CmdStatus equal to "Approved".

```Java
try {
	JSONObject jsonObject = new JSONObject(response);
			
	if (jsonObject.has("CmdStatus") && jsonObject.getString("CmdStatus").equals("Approved")) {
		// Approved logic here
	} 
	else {
		// Declined logic here
	}
}
catch (Exception e) {
	e.printStackTrace();
}
```

###© 2014 Mercury Payment Systems, LLC - all rights reserved.

Disclaimer:
This software and all specifications and documentation contained herein or provided to you hereunder (the "Software") are provided free of charge strictly on an "AS IS" basis. No representations or warranties are expressed or implied, including, but not limited to, warranties of suitability, quality, merchantability, or fitness for a particular purpose (irrespective of any course of dealing, custom or usage of trade), and all such warranties are expressly and specifically disclaimed. Mercury Payment Systems shall have no liability or responsibility to you nor any other person or entity with respect to any liability, loss, or damage, including lost profits whether foreseeable or not, or other obligation for any cause whatsoever, caused or alleged to be caused directly or indirectly by the Software. Use of the Software signifies agreement with this disclaimer notice.

[![Analytics](https://ga-beacon.appspot.com/UA-1785046-21/REST.Android/readme?pixel)](https://github.com/MercuryPay)
