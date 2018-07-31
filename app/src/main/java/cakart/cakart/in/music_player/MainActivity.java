package cakart.cakart.in.music_player;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button play,pause,stop;
    MediaPlayer mediaPlayer;
    int pausecurrentposition;
    private SeekBar positionBar,volumeBar;
    private TextView elapsedtimelabel,remainingtimelabel;
    private int totaltime;
    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play=(Button)findViewById(R.id.play_id);
        pause=(Button)findViewById(R.id.pause_id);
        stop=(Button)findViewById(R.id.stop_id);

        elapsedtimelabel=(TextView)findViewById(R.id.elapsedtimelabel);
        remainingtimelabel=(TextView)findViewById(R.id.remainingtimelabel);

        positionBar=(SeekBar)findViewById(R.id.position_id);
        volumeBar=(SeekBar)findViewById(R.id.volume_id);

        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.play_id:
                if(mediaPlayer==null){
                mediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.music_song);
                mediaPlayer.start();
                mediaPlayer.seekTo(0);
                mediaPlayer.setVolume(0.5f,0.5f);
                mediaPlayer.setLooping(true);
                totaltime=mediaPlayer.getDuration();
                positionBar.setMax(totaltime);
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                   public void run() {
                       if(mediaPlayer!=null && mediaPlayer.isPlaying()==true){
                            //timer.cancel();
                         //   return;
                          // mediaPlayer.pause();
                        }
                        else {
                           mediaPlayer=mediaPlayer.create(getApplicationContext(),R.raw.music_song);
                       }
                        final int t = (mediaPlayer.getCurrentPosition())/1000;
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                elapsedtimelabel.setText(""+( String.format("%02d:%02d", t / 60, t % 60)));
                                positionBar.setProgress(mediaPlayer.getCurrentPosition());

                                int remaining = (totaltime - mediaPlayer.getCurrentPosition())/1000;
                                //Log.d("Akhil2",totaltime+"");
                                //Log.d("Akhil",remaining+"");

                                remainingtimelabel.setText(""+String.format("%02d:%02d",remaining/60,remaining%60));
                                positionBar.setProgress(mediaPlayer.getCurrentPosition());
                            }
                        });

                    }
                },0,1000);
                positionBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromuser) {
                        if(fromuser){
                            mediaPlayer.seekTo(progress);
                            positionBar.setProgress(progress);
                        }
                        int t = (mediaPlayer.getCurrentPosition())/1000;
                        elapsedtimelabel.setText(""+( String.format("%02d:%02d", t / 60, t % 60)));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromuser) {
                    float volumenumber=progress/100f;
                    mediaPlayer.setVolume(volumenumber,volumenumber);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                }
                else if(!mediaPlayer.isPlaying())
                {
                    mediaPlayer.seekTo(pausecurrentposition);
                    mediaPlayer.start();
                }
                break;
            case R.id.pause_id:
                if(mediaPlayer!=null){
                    mediaPlayer.pause();
                    pausecurrentposition=mediaPlayer.getCurrentPosition();
                }
                break;
            case R.id.stop_id:
                if (mediaPlayer!=null){
                mediaPlayer.stop();
                mediaPlayer=null;
                }
                break;
        }
    }
}
