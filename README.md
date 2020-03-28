
  
  
  
  
# Payment Gateway Integration  
[![JitPack Build](https://jitpack.io/v/TanJay/payment_library.svg)](https://jitpack.io/#TanJay/payment_library)  
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/TanJay/payment_library.svg)  
![GitHub language count](https://img.shields.io/github/languages/count/TanJay/payment_library.svg)  
![JitPack - Downloads](https://img.shields.io/jitpack/dm/github/TanJay/payment_library.svg)  
![GitHub issues](https://img.shields.io/github/issues/TanJay/payment_library.svg)  
![GitHub last commit](https://img.shields.io/github/last-commit/TanJay/payment_library.svg)  
![GitHub](https://img.shields.io/github/license/TanJay/payment_library.svg)  
![Uptime Robot status](https://img.shields.io/uptimerobot/status/m782945181-ad584048798a2d55463d26c8.svg)  
![GitHub Release Date](https://img.shields.io/github/release-date/TanJay/payment_library.svg)  
[![Build Status](https://travis-ci.org/TanJay/payment_library.svg?branch=master)](https://travis-ci.org/TanJay/payment_library)  
  
Hi, Currently we have only integrated **Genie** payemnt gateway.  
  
# Demo Application  
  
![Payment Screen](https://i.ibb.co/pdgtF6B/PHOTO-2020-03-29-03-11-25-2.jpg)
![Payment Screen](https://i.ibb.co/jyk2HTt/PHOTO-2020-03-29-03-11-25-1.jpg)
![Payment Screen](https://i.ibb.co/kKmVZ9g/PHOTO-2020-03-29-03-11-26.jpg)
https://ibb.co/D9fyLtd
https://ibb.co/D4rPd8D  
  
*[Payment Screen] Screenshot*  
  
# Installation  
  
Add it in your root *build.gradle* at the end of repositories  
  
```javascript  
allprojects {  
      repositories {  
         ...  
         maven { url 'https://jitpack.io' }  
      }  
   }  
```  
**Step 2.** Add the *dependency*  
```javascript  
dependencies {  
      ...  
        implementation 'com.github.TanJay:payment_library:0.0.2'  
   }  
```  
 Simple Build will install all needed dependencies  
  
## Setup  
  
**Step 1**: Add the following permissions  to *AndroidManifest.xml*  
```xml  
<uses-permission android:name="android.permission.WAKE_LOCK" />  
<uses-permission android:name="android.permission.INTERNET" />  
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />  
<uses-permission android:name="android.permission.READ_PHONE_STATE" />  
```  
**Step 2**: Add the metadata  to *AndroidManifest.xml* under <code>\<application\></code> tag  
```xml  
<meta-data android:name="pg_identifier" android:value="@string/pg_identifier" />  
<meta-data android:name="store_name" android:value="@string/store_name" />  
<meta-data android:name="secret_code" android:value="@string/secret_code" />  
<meta-data android:name="currency" android:value="@string/currency" />  
<meta-data android:name="pub_port" android:value="@string/pub_port" />  
<meta-data android:name="pub_server" android:value="@string/pub_server" />  
<meta-data android:name="pub_username" android:value="@string/pub_username" />  
<meta-data android:name="pub_password" android:value="@string/pub_password" />  
<meta-data android:name="callback" android:value="@string/callback" />  
<meta-data android:name="merchant_identifier" android:value="@string/merchant_identifier" />  
<meta-data android:name="token_store_name" android:value="@string/token_store_name" />  
<meta-data android:name="language" android:value="@string/language" />  
<meta-data android:name="card_save_type" android:value="@string/card_save_type" />  
<meta-data android:name="merchant_display" android:value="@string/merchant_display" />  
<meta-data android:name="banner_hide" android:value="@string/banner_hide" />
```  
  
**Step 3**: Add the following service to  *AndroidManifest.xml* under <code>\<application\></code> tag  
```xml  
//Soon Be removed
<service android:name="org.eclipse.paho.android.service.MqttService" >  
</service>  
```  
  
**Step 4**: Add the following under *AndroidManifest.xml* inside <code>\<application\></code> tag on top  
```xml  
android:usesCleartextTraffic="true"  
```  
  
**Step 5**: Add buildConfig Variables to *gradle.build* (module: app)  
```javascript  
android {  
    ...  
    buildTypes {  
       ...  
       //This is needed if you want to add the values in Debug Build  
        debug {  
            ...  
            resValue 'string', "pg_identifier", (rootProject.findProperty("payment_pg_identifier") ?: "0")  
			resValue 'string', "store_name", (rootProject.findProperty("payment_store_name") ?: "0")  
			resValue 'string', "secret_code", (rootProject.findProperty("payment_secret_code") ?: "0")  
			resValue 'string', "currency",(rootProject.findProperty("payment_currency") ?: "0")  
			resValue 'string', "pub_port", (rootProject.findProperty("payment_pub_port") ?: "0")  
			resValue 'string', "pub_server", (rootProject.findProperty("payment_pub_server") ?: "0")  
			resValue 'string', "pub_username", (rootProject.findProperty("payment_pub_username") ?: "0")  
			resValue 'string', "pub_password", (rootProject.findProperty("payment_pub_password") ?: "0")  
			resValue 'string', "callback", (rootProject.findProperty("payment_callback") ?: "0")  
			resValue 'string', "merchant_identifier", (rootProject.findProperty("tokenization_merchant_identifier") ?: "0")  
			resValue 'string', "token_store_name", (rootProject.findProperty("tokenization_store_name") ?: "0")  
			resValue 'string', "language", (rootProject.findProperty("tokenization_language") ?: "0")  
			resValue 'string', "card_save_type", (rootProject.findProperty("tokenization_card_save_type") ?: "0")  
			resValue 'string', "merchant_display", (rootProject.findProperty("tokenization_merchant_display") ?: "0")  
			resValue 'string', "banner_hide", (rootProject.findProperty("banner_hide") ?: "0")  
       //This is needed if you want to add the values in Debug Build  
        release {  
         ...  
         resValue 'string', "pg_identifier", (rootProject.findProperty("payment_pg_identifier") ?: "0")  
		resValue 'string', "store_name", (rootProject.findProperty("payment_store_name") ?: "0")  
		resValue 'string', "secret_code", (rootProject.findProperty("payment_secret_code") ?: "0")  
		resValue 'string', "currency",(rootProject.findProperty("payment_currency") ?: "0")  
		resValue 'string', "pub_port", (rootProject.findProperty("payment_pub_port") ?: "0")  
		resValue 'string', "pub_server", (rootProject.findProperty("payment_pub_server") ?: "0")  
		resValue 'string', "pub_username", (rootProject.findProperty("payment_pub_username") ?: "0")  
		resValue 'string', "pub_password", (rootProject.findProperty("payment_pub_password") ?: "0")  
		resValue 'string', "callback", (rootProject.findProperty("payment_callback") ?: "0")  
		resValue 'string', "merchant_identifier", (rootProject.findProperty("tokenization_merchant_identifier") ?: "0")  
		resValue 'string', "token_store_name", (rootProject.findProperty("tokenization_store_name") ?: "0")  
		resValue 'string', "language", (rootProject.findProperty("tokenization_language") ?: "0")  
		resValue 'string', "card_save_type", (rootProject.findProperty("tokenization_card_save_type") ?: "0")  
		resValue 'string', "merchant_display", (rootProject.findProperty("tokenization_merchant_display") ?: "0")  
		resValue 'string', "banner_hide", (rootProject.findProperty("banner_hide") ?: "0") 
        }  
    }  
}  
```  
  
**Step 6**: Add buildConfigs to *gradle.properties* on <code>C:\\Users\\\<Your Username>\\.gradle</code> or <code>/Users/\<Your Username>/.gradle</code>  
```css    
payment_pg_identifier=""    
payment_store_name=""    
payment_secret_code=""    
payment_currency=""    
payment_pub_port=""    
payment_pub_server=""    
payment_pub_username=""    
payment_pub_password=""    
payment_callback=""    
tokenization_merchant_identifier=""
tokenization_store_name=""
tokenization_language=""
tokenization_card_save_type=""
tokenization_merchant_display=""
banner_hide=""
```    
    
## Usage    
    
Java Implementation    
```java    
//Init the view with the price you need    
GeniePayment.processPayment(MainActivity.this, "1.00");

// Save the credit card
GeniePayment.processSaveCard(MainActivity.this);

// Save card with initial transaction
GeniePayment.processSaveCardWithInitialTransaction(MainActivity.this, "1.00");

//Listen for a successful callback from the Observerable    
GeniePayment.getListner(this).getCurrentName().observe(this, new Observer<TransactionResponse>() {  
    @Override  
	public void onChanged(@Nullable TransactionResponse result) {  
        if(result.getStatus()){  
            Toast.makeText(getApplicationContext(), String.format("Failed: %s", result.getMessage()), Toast.LENGTH_LONG).show();  
		  } else {  
		            Toast.makeText(getApplicationContext(), String.format("Success: %s", result.getMessage()), Toast.LENGTH_LONG).show();  
		  }  
		  GeniePayment.dismiss();  
	}  
});
```    
    
kt Implementation    
```kotlin    
//Init the view with the price you need    **(Will Update Soon)**
        GeniePayment.Process(this, "1.00")    
    
        //Listen for a successful callback from the Observerable    
        GeniePayment.getListner(this).currentName.observe(this, Observer<String> { t ->    
            Log.d("Genie - Result", String.format("Your result is %s", t))    
            if (t == "1") {    
                //Successful    
                GeniePayment.dismiss()    
            } else if (t == "0") {    
                //Failed    
                GeniePayment.dismiss()    
            } else {    
                //Nothing happened    
            }    
        })    
```
