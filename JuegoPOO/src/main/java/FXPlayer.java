import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;


public enum FXPlayer {
   ROBO_COP("ROBO_COP.wav"),
   CHASE("CHASE.wav"),
   DRAMATIC("DRAMATIC.wav"),
   FIGHT("FIGHT.wav"),
   ACTIVO("CHASE.wav");
   
   public static enum Volume {
      MUTE, LOW, MEDIUM, HIGH
   }

   public static Volume volume = Volume.LOW;

   private Clip clip;

   FXPlayer(String wav) {
      try {

         URL url = this.getClass().getClassLoader().getResource("musica/"+wav);

         AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);

         clip = AudioSystem.getClip();

         clip.open(audioInputStream);
      } catch (UnsupportedAudioFileException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      } catch (LineUnavailableException e) {
         e.printStackTrace();
      }
   }


   public void play() {
      if (volume != Volume.MUTE) {
         if (!clip.isRunning()){
               clip.setFramePosition(0);
         		clip.start();
         }
      }
   }

   public void stop(){
      if (volume != Volume.MUTE) {
         if (clip.isRunning()){
              clip.setFramePosition(0);
               clip.stop();
         }
      }
   }

   public void loop(){
       if (volume != Volume.MUTE) {
         if (!clip.isRunning()){
              clip.loop(Clip.LOOP_CONTINUOUSLY);
         }
      }
   }
 

   static void init() {
      values();
   }
}