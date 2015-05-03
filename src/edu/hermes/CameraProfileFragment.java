package edu.hermes;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class CameraProfileFragment extends Fragment{
	private static final String TAG ="CameraProfileFragment";
	private Camera mCamera;
	public static final String EXTRA_PHOTO_FILENAME ="edu.hermes.CameraProfileFragment";
	
	private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
		
		@Override
		public void onShutter() {
			
		}
	};
	private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			String filename = UUID.randomUUID().toString()+ ".jpg";
			FileOutputStream outStream = null;
			boolean success = true;
			try {
				outStream = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
				outStream.write(data);
			} catch (IOException e) {
				e.printStackTrace();
				success = false;
			}finally{
				try {
					if(outStream != null){
						outStream.close();
					} 
				} catch (Exception e) {
					e.printStackTrace();
					success = false;
				}
			}
			if(success){
				Intent i = new Intent();
				i.putExtra(EXTRA_PHOTO_FILENAME, filename);
				getActivity().setResult(Activity.RESULT_OK,i);
				getActivity().finish();
			}else{
				getActivity().setResult(Activity.RESULT_CANCELED);
			}
		}
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_camera_profile, container,false);
		Button takePictureBtn = (Button) v.findViewById(R.id.cameraBtn_fragment_camera_profile);
		takePictureBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mCamera!=null){
					mCamera.takePicture(mShutterCallback, null, mPictureCallback);
				}
			}
			
		});
		SurfaceView surfaceView = (SurfaceView) v.findViewById(R.id.surfaceView_fragment_camera_profile);
		SurfaceHolder holder = surfaceView.getHolder();
		holder.addCallback(new Callback() {
			
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				if(mCamera !=null){
					mCamera.stopPreview();
				}
			}
			
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				try {
					if(mCamera !=null){
						mCamera.setPreviewDisplay(holder);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
				if(mCamera == null) return;
				Camera.Parameters parameters = mCamera.getParameters();
				try {
					mCamera.startPreview();
				} catch (Exception e) {
					e.printStackTrace();
					mCamera.release();
					mCamera = null;
				}
			}
		});
		return v;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mCamera = Camera.open();
	}
	@Override
	public void onPause() {
		super.onPause();
		if(mCamera !=null){
			mCamera.release();
			mCamera =null;
		}
	}
}
