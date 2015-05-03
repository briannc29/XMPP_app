package edu.hermes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.services.HermesService;
import edu.services.MainService;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.text.Layout.Directions;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Filter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class IndividualConversationFragment extends Fragment{
	public static final String ACTION_SEND_MESSAGE = "edu.hermes.IndividualCoversationFragment.SEND_MESSAGE";
	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_FILE =1;
	private Uri imageUri;
	BuddyInfo buddy;
	ListView listView;
	EditText text;
	ListView listConv;
	ArrayList<Conversation> dataALConv = new ArrayList<Conversation>();
	MainService mainService;
	private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String from = (String) intent.getStringExtra("from");
			String nameOnly = from.split("@")[0];
			if(buddy.getUsername().equals(nameOnly)){
				String body = intent.getStringExtra("body");
				dataALConv.add(new Conversation(nameOnly, Calendar.getInstance().getTime().toString(), body, null));
				setListAdapter();
				ConversationLab.getInstance(getActivity().getApplicationContext()).addConversation(new Conversation(nameOnly, Calendar.getInstance().getTime().toString(), body,null));
			}
			
		}
	};
	private BroadcastReceiver fileReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String uri = intent.getStringExtra("uri");
			String user = intent.getStringExtra("user").split("@")[0];
			dataALConv.add(new Conversation(user, Calendar.getInstance().getTime().toString(), null, Uri.parse(uri)));
			setListAdapter();
		}
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		buddy = (BuddyInfo) getActivity().getIntent().getSerializableExtra("buddy");
		getActivity().setTitle("Chat to "+buddy.getUsername());
		mainService = MainService.getInstance(getActivity().getApplicationContext());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_individual_conversation, container,false);
		listConv = (ListView) v.findViewById(R.id.listMessages_indi_conv);
		text = (EditText) v.findViewById(R.id.chatET_indi_conv);
		Button sendBtn = (Button) v.findViewById(R.id.sendBtn_indi_conv);
		sendBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String newInputMessage = text.getText().toString();
				if(!newInputMessage.equals("")){
					mainService.sendMessage(buddy.getUsername(), text.getText().toString().trim());
					dataALConv.add(new Conversation(null, Calendar.getInstance().getTime().toString(), text.getText().toString(), null));
					setListAdapter();
					text.setText("");
				}
			}
		});
		setListAdapter();
		return v;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_individual_conversation, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_send_file:
			selectFileAndSend();
			return true;
		case R.id.action_take_photo:
			takePhoto();
			return true;
		case R.id.action_take_video:
			takeVideo();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
		
		
	}
	
	private class ConversationAdapter extends ArrayAdapter<Conversation>{
		public ConversationAdapter(ArrayList<Conversation> data) {
			super(getActivity(), android.R.layout.simple_list_item_1,data);
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = getActivity().getLayoutInflater().inflate(R.layout.list_indi_conv_item, null);
			};
			Conversation conversation = dataALConv.get(position);
			TextView friendTV = (TextView) convertView.findViewById(R.id.friend_indi_conv);
			TextView youTV = (TextView) convertView.findViewById(R.id.yourself_indi_conv);
			TextView textLeft = (TextView) convertView.findViewById(R.id.left_text_list_indi_conv_item);
			TextView textRight = (TextView) convertView.findViewById(R.id.right_text_list_indi_conv_item);
			
			ImageView image = (ImageView) convertView.findViewById(R.id.image_indi_conv_item);
			if(conversation.getUri()!=null){
//				ContentResolver cr = getActivity().getContentResolver();
//				Bitmap bitmap;
//				try {
//					bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, conversation.getUri());
//					image.setImageBitmap(bitmap);
//				} catch (IOException e) {
//					e.printStackTrace(); 
//				}
				Uri uri = conversation.getUri(); 
				File file = new File(uri.getPath());
				String ext = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
				if(ext.equals("jpg")||ext.equals("png")){
					
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(480, 480);
					image.setLayoutParams(params);
					image.setImageURI(conversation.getUri());
				}else {
					image.setMaxHeight(120);
					image.setMaxWidth(120);
					image.setImageDrawable(getResources().getDrawable(R.drawable.file_received));
				}
			}
			if(conversation.getPartner()!=null){
				friendTV.setText(conversation.getPartner());
				youTV.setVisibility(View.INVISIBLE);
				textLeft.setText(conversation.getText());
			}else{
				youTV.setText("You");
				textRight.setText(conversation.getText());
			}
			return convertView;
		}
		
	}
	
	private void setListAdapter(){
		listConv.setAdapter(new ConversationAdapter(dataALConv));
		listConv.setSelection(dataALConv.size()-1);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter(HermesService.ACTION_NEW_MESSAGE);
		getActivity().registerReceiver(messageReceiver,filter);
		
		IntentFilter fileFilter = new IntentFilter(HermesService.ACTION_NEW_FILE);
		getActivity().registerReceiver(fileReceiver, fileFilter);
	}
	@Override
	public void onPause() {
		super.onPause();
		getActivity().unregisterReceiver(messageReceiver);
		getActivity().unregisterReceiver(fileReceiver);
	}
	
	private void selectFileAndSend() {
		Intent i = new Intent(Intent.ACTION_GET_CONTENT);
		i.setType("*/*");
		startActivityForResult(i, CHOOSE_FILE);
	}
	public void takePhoto (){
		Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		File photo = new File(Environment.getExternalStorageDirectory()+"/DCIM",new Date().getTime()+".png");
		i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
		imageUri = Uri.fromFile(photo);
		startActivityForResult(i, TAKE_PICTURE);
	}
	public void takeVideo (){
		Intent i = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
		File video = new File(Environment.getExternalStorageDirectory()+"/DCIM",new Date().getTime()+".mp4");
		i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(video));
		imageUri = Uri.fromFile(video);
		startActivityForResult(i, TAKE_PICTURE);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case TAKE_PICTURE:
			if(resultCode == Activity.RESULT_OK){
				Uri selectedImage = imageUri;
				getActivity().getContentResolver().notifyChange(selectedImage, null);
				dataALConv.add(new Conversation(null, Calendar.getInstance().getTime().toString(), null, selectedImage));
				setListAdapter();
				mainService.sendFile(selectedImage, buddy.getUsername()+"@proj-309-09");
			}
			break;
		case CHOOSE_FILE:
			if(resultCode == Activity.RESULT_OK){
				Uri fileUri = data.getData();
				mainService.sendFile(fileUri, buddy.getUsername()+"@proj-309-09");
			}
			break;
		default:
			break;
		}
	}
}




