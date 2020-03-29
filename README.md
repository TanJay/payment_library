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
*[Payment Screen] Card Save*  
![Payment Screen](https://i.ibb.co/jyk2HTt/PHOTO-2020-03-29-03-11-25-1.jpg)
*[Payment Screen] Payment Process*  
![Payment Screen](https://i.ibb.co/kKmVZ9g/PHOTO-2020-03-29-03-11-26.jpg)
*[Payment Screen] Card Save with Initial charge*  
  
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
        implementation 'com.github.TanJay:payment_library:0.0.3'  
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
<meta-data android:name="callback" android:value="@string/callback" />  
<meta-data android:name="merchant_identifier" android:value="@string/merchant_identifier" />  
<meta-data android:name="token_store_name" android:value="@string/token_store_name" />  
<meta-data android:name="language" android:value="@string/language" />  
<meta-data android:name="card_save_type" android:value="@string/card_save_type" />  
<meta-data android:name="merchant_display" android:value="@string/merchant_display" />  
<meta-data  android:name="url_trigger" android:value="@string/url_trigger" />  
<meta-data android:name="banner_hide" android:value="@string/banner_hide" />
```  
  
**Step 3**: Add strings to values  *strings.xml*   
```xml  
<string name="banner_hide" translatable="false">value</string>
<string name="callback" translatable="false">value</string>
<string name="card_save_type" translatable="false">value</string>
<string name="currency" translatable="false">value</string>
<string name="language" translatable="false">value</string>
<string name="merchant_display" translatable="false">value</string>
<string name="merchant_identifier" translatable="false">value</string>
<string name="pg_identifier" translatable="false">value</string>
<string name="secret_code" translatable="false">value</string>
<string name="store_name" translatable="false">value</string>
<string name="token_store_name" translatable="false">value</string>
<string name="url_trigger" translatable="false">value</string> 
```  
  
**Step 4**: Add the following under *AndroidManifest.xml* inside <code>\<application\></code> tag on top  
```xml  
android:usesCleartextTraffic="true"  
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
            Toast.makeText(getApplicationContext(), String.format("Success: %s", result.getMessage()), Toast.LENGTH_LONG).show();  
		} else {  
		    Toast.makeText(getApplicationContext(), String.format("Failed: %s", result.getMessage()), Toast.LENGTH_LONG).show();  
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
