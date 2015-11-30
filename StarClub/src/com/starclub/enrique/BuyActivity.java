package com.starclub.enrique;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.UserDictionary;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;





import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.inapppurcharse.util.IabHelper;
import com.inapppurcharse.util.IabResult;
import com.inapppurcharse.util.Inventory;
import com.inapppurcharse.util.Purchase;
import com.starclub.enrique.R;
import com.mycom.data.Global;
import com.mycom.data.MyConstants;
import com.mycom.lib.Const;
import com.mycom.lib.UserDefault;
import com.network.httpclient.Utils;

public class BuyActivity extends Activity implements OnClickListener{
    
    
    /*
     *   in app billing
     */
	private Context mContext;
	private static final String TAG = "BuyActivity";
	
    private static final String LICENSE_KEY = MyConstants.InAppPurchase_LICENSE_KEY;

	public String PURCHASE_ITEM1 = MyConstants.InAppPurchase_ID; //"android.test.purchased";
	
//    private BillingProcessor bp;
//    private boolean readyToPurchase = false;
	
    // The helper object
    IabHelper mHelper;

    static final int RC_REQUEST = 10001;

    
    
    
    private ToggleButton btnCredit100, btnCredit1000, btnCredit2500, btnCredit5000;
    int m_nSelBtn = 0;
    TextView tvCreditLeft = null;
    
    ProgressDialog progress = null;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        
        setContentView(R.layout.activity_buy);
        
        mContext = this;
        
        UserDefault.init(this);
        
        
    	StarTracker.StarSendView(this, "StarCredits");


/*        
        // credit
        Intent intent = new Intent(this, PayPalService.class);
        
        intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, CONFIG_ENVIRONMENT);
        intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, CONFIG_CLIENT_ID);
        intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL, CONFIG_RECEIVER_EMAIL);
        
        startService(intent);
*/
    	
/*    	
        // in app billing v2
        Log.i("BillingService", "Starting");
        mContext = this;
        startService(new Intent(mContext, BillingService.class));
        BillingHelper.setCompletedHandler(mTransactionHandler);
*/
        
    	// in app billing v3
//    	bp = new BillingProcessor(this, LICENSE_KEY, new BillingProcessor.IBillingHandler() {
//            @Override
//            public void onProductPurchased(String productId) {
//                showToast("onProductPurchased: " + productId);
//                updateTextViews();
//            }
//            @Override
//            public void onBillingError(int errorCode, Throwable error) {
//                showToast("onBillingError: " + Integer.toString(errorCode));
//            }
//            @Override
//            public void onBillingInitialized() {
//                readyToPurchase = true;
////                updateTextViews();
//            }
//            @Override
//            public void onPurchaseHistoryRestored() {
//                showToast("onPurchaseHistoryRestored");
//                for(String sku : bp.listOwnedProducts())
//                    Log.d(TAG, "Owned Managed Product: " + sku);
//                for(String sku : bp.listOwnedSubscriptions())
//                    Log.d(TAG, "Owned Subscription: " + sku);
////                updateTextViews();
//            }
//        });
    	
    	String base64EncodedPublicKey = LICENSE_KEY;
    	
    	// Create the helper, passing it our context and the public key to verify signatures with
        Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        // enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(true);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                	Log.d(TAG, "Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });
    	
        setInterface();
        
    }

 // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
            	Log.d(TAG, "Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

            // Check for gas delivery -- if we own gas, we should fill up the tank immediately
            Purchase gasPurchase = inventory.getPurchase(PURCHASE_ITEM1);
            if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                Log.d(TAG, "We have gas. Consuming it.");
                mHelper.consumeAsync(inventory.getPurchase(PURCHASE_ITEM1), mConsumeFinishedListener);
                return;
            }

            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };

    
    
    public void setInterface() {
    	Button btnBack = (Button) findViewById(R.id.btnBack);
    	btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				StarTracker.StarSendEvent(mContext, "App Event", "Credits", "Cancelled");
				
			 	Intent returnIntent = new Intent();
	    		setResult(RESULT_OK, returnIntent);        
	    		finish();
			}
		});
    	
    	Button btnNext = (Button) findViewById(R.id.btnNext);
    	btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onInappBilling();
//				onBuyPressed();
			}
		});
    	
    	btnCredit100 = (ToggleButton) findViewById(R.id.btnCredit100);
    	btnCredit100.setOnClickListener(this);
    	btnCredit1000 = (ToggleButton) findViewById(R.id.btnCredit1000);
    	btnCredit1000.setOnClickListener(this);
    	btnCredit2500 = (ToggleButton) findViewById(R.id.btnCredit2500);
    	btnCredit2500.setOnClickListener(this);
    	btnCredit5000 = (ToggleButton) findViewById(R.id.btnCredit5000);
    	btnCredit5000.setOnClickListener(this);
    	
    	tvCreditLeft = (TextView) findViewById(R.id.tvCreditLeft);
    	JSONObject userInfo = UserDefault.getDictionaryForKey("user_info");
    	String creditLeft = "";
		try {
			creditLeft = userInfo.getString("credit");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	tvCreditLeft.setText(creditLeft);
    }
    
	@Override
	public void onClick(View v) {
	
		switch (v.getId()) {
		case R.id.btnCredit100:
			m_nSelBtn = 0;
			break;
			
		case R.id.btnCredit1000:
			m_nSelBtn = 1;
			break;

		case R.id.btnCredit2500:
			m_nSelBtn = 2;
			break;

		case R.id.btnCredit5000:
			m_nSelBtn = 3;
			break;

		default:
			break;
		}

		changeButtonState();
	}
	private void changeButtonState() {
		if (m_nSelBtn == 0) {
			btnCredit100.setChecked(true);
			btnCredit1000.setChecked(false);
			btnCredit2500.setChecked(false);
			btnCredit5000.setChecked(false);
		}
		if (m_nSelBtn == 1) {
			btnCredit100.setChecked(false);
			btnCredit1000.setChecked(true);
			btnCredit2500.setChecked(false);
			btnCredit5000.setChecked(false);
		}
		if (m_nSelBtn == 2) {
			btnCredit100.setChecked(false);
			btnCredit1000.setChecked(false);
			btnCredit2500.setChecked(true);
			btnCredit5000.setChecked(false);
		}
		if (m_nSelBtn == 3) {
			btnCredit100.setChecked(false);
			btnCredit1000.setChecked(false);
			btnCredit2500.setChecked(false);
			btnCredit5000.setChecked(true);
		}
	}
	
    
 
    private void onInappBilling() {
/* v2    	
    	if(BillingHelper.isBillingSupported()){
			if (m_nSelBtn == 0) {
				BillingHelper.requestPurchase(mContext, PURCHASE_ITEM1);
			}
//			else if (m_nSelBtn == 1) {
//				BillingHelper.requestPurchase(mContext, PURCHASE_ITEM2);
//			}
//			else if (m_nSelBtn == 2) {
//				BillingHelper.requestPurchase(mContext, PURCHASE_ITEM3);
//			}
//			else if (m_nSelBtn == 3) {
//				BillingHelper.requestPurchase(mContext, PURCHASE_ITEM4);
//			}
			
			// android.test.purchased or android.test.canceled or android.test.refunded or com.blundell.item.passport
        } else {
        	Log.i(TAG,"Can't purchase on this device");
        	// XXX press button before service started will disable when it shouldnt
        }
*/
    	//v3
//    	if (!readyToPurchase) {
//            showToast("Billing not initialized.");
//            return;
//        }
//    	
//    	bp.purchase(PURCHASE_ITEM1);
    	
         Log.d(TAG, "Launching purchase flow for gas.");
         
    	/* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";

        mHelper.launchPurchaseFlow(this, PURCHASE_ITEM1, RC_REQUEST,
                mPurchaseFinishedListener, payload);
    }
    
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
    	 
    	if (mHelper == null) return;
    	
        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        }
        else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
        

    }
    
    /** Verifies the developer payload of a purchase. */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true;
    }
    
 // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
            	Log.d(TAG, "Error purchasing: " + result);
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
            	Log.d(TAG, "Error purchasing. Authenticity verification failed.");
                return;
            }

            Log.d(TAG, "Purchase successful.");

            if (purchase.getSku().equals(PURCHASE_ITEM1)) {
                // bought 1/4 tank of gas. So consume it.
                Log.d(TAG, "Purchase is gas. Starting gas consumption.");
                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
            }

        }
    };

    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            // We know this is the "gas" sku because it's the only one we consume,
            // so we don't check which sku was consumed. If you have more than one
            // sku, you probably should check...
            if (result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
                Log.d(TAG, "Consumption successful. Provisioning.");
                
                StarTracker.StarSendEvent(mContext, "App Event", "Credits", ""+result);
                
                updateTextViews();
                
            }
            else {
            	Log.d(TAG, "Error while consuming: " + result);
            }
            Log.d(TAG, "End consumption flow.");
        }
    };
    
    @Override
    public void onDestroy() {
//        stopService(new Intent(this, PayPalService.class));
    	
//        BillingHelper.stopService();
    	
//        if (bp != null)
//            bp.release();

        super.onDestroy();
        
        Log.d(TAG, "Destroying helper.");
        if (mHelper != null) {
            mHelper.dispose();
            mHelper = null;
        }
        
    }
    
    /*
     *  in app billing
     */
/* v2    
    public Handler mTransactionHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			Log.i(TAG, "Transaction complete");
			Log.i(TAG, "Transaction status: "+BillingHelper.latestPurchase.purchaseState);
			Log.i(TAG, "Item purchased is: "+BillingHelper.latestPurchase.productId);
			
			if(BillingHelper.latestPurchase.isPurchased()){
				Log.i(TAG, "Item purchased is OK ============= ");
				
				if (m_nSelBtn == 0) {
					JSONObject userInfo = UserDefault.getDictionaryForKey("user_info");
					
					int oldCredit = 0;
					try {
						oldCredit = userInfo.getInt("credit");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					int newCredit = oldCredit + 100;
					
					updateCredit(newCredit);
				}
			}
		};
	
    };
*/
    int m_nCredit = 0;
    
	public void updateCredit(int credit) {
		
		progress = new ProgressDialog(this);
		progress.setCancelable(false);
		progress.setMessage("Updating...");
    	
		progress.show();
    	
		m_nCredit = credit;
		
		new Thread(){
			public void run(){
		    	
		    	HttpParams myParams = new BasicHttpParams();

				 HttpConnectionParams.setConnectionTimeout(myParams, 30000);
				 HttpConnectionParams.setSoTimeout(myParams, 30000);
				 
				 DefaultHttpClient hc= new DefaultHttpClient(myParams);
				 ResponseHandler <String> res=new BasicResponseHandler();
				
				 String url = Utils.getUpdateUrl();
				 HttpPost postMethod = new HttpPost(url);
				

				 String responseBody = "";
				 try {
				 MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

				 reqEntity.addPart("cid",	 		new StringBody("" + MyConstants.CID));
				 reqEntity.addPart("token",	 		new StringBody("" + Utils.getUserToken()));
				 reqEntity.addPart("user_id", 		new StringBody("" + Utils.getUserID()));
				 
				 reqEntity.addPart("credit",	 	new StringBody("" + m_nCredit));
				 
				 postMethod.setEntity(reqEntity);
				 responseBody = hc.execute(postMethod,res);
				 
				 System.out.println("update result = " + responseBody);
				 JSONObject result = new JSONObject(responseBody);
					if (result != null) {
						boolean status = result.optBoolean("status"); 
						if (status) {
							
							m_handler.sendEmptyMessage(1);
							
							return;
						}
					}
				 } catch (UnsupportedEncodingException e) {
					 e.printStackTrace();
				 } catch (ClientProtocolException e) {
					 e.printStackTrace();
				 } catch (IOException e) {
					 e.printStackTrace();
				 } catch (JSONException e) {
				 }
				 
				 m_handler.sendEmptyMessage(-1);
			}
		}.start();
	}
	
	
	public Handler m_handler = new Handler() {
		
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			progress.hide();
			
			switch (msg.what) {
			case 0:
				
				break;
				
			case 1: 

				JSONObject userInfo = UserDefault.getDictionaryForKey("user_info");
				
				try {
					userInfo.put("credit", m_nCredit);
					UserDefault.setDictionaryForKey(userInfo, "user_info");

					if (tvCreditLeft != null)
						tvCreditLeft.setText("" + m_nCredit);
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				StarTracker.StarSendEvent(mContext, "App Event", "Credits", "Update DB Error");
				
				onAutoFinish();
				
				break;
				
			case -1:
				
				StarTracker.StarSendEvent(mContext, "App Event", "Credits", "Update DB Error");
				
				Const.showMessage("", "Network is failed.", BuyActivity.this);
				break;
			
			}
			
		}
   };
   
   public void onAutoFinish() {
	   
	   new Handler().post(new Runnable() {
		   
	        @Override
	        public void run() {

	 		   try {
	 			   Thread.sleep(2000);
	 		   } catch (InterruptedException e) {
	 			   // TODO Auto-generated catch block
	 			   e.printStackTrace();
	 		   }

	        	Intent returnIntent = new Intent();
	    		setResult(RESULT_OK, returnIntent);        
	    		finish();
	        }
	    });
	   
	   
   }
   
   private void updateTextViews() {
	   if (m_nSelBtn == 0) {
			JSONObject userInfo = UserDefault.getDictionaryForKey("user_info");
			
			int oldCredit = 0;
			try {
				oldCredit = userInfo.getInt("credit");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			int newCredit = oldCredit + 100;
			
			
			updateCredit(newCredit);
		}
   }
   private void showToast(String message) {
       Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
   }
}
