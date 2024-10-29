import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Project3 {
    public static ArrayList<Song> allSongs=new ArrayList<>();
    public static ArrayList<PlayList> allPlayLists= new ArrayList<>();
    public static void main(String[] args) throws IOException {
        //taking args from command line to determine which files are read and write
        String songFile;
        String testCaseFile;
        String outputFile;
        songFile = args[0];
        testCaseFile=args[1];
        outputFile = args[2];
        FileWriter writer = new FileWriter(outputFile);
        try {


            File myObj = new File(songFile);
            Scanner myReader = new Scanner(myObj);
            //adding songs to allSongs arraylist from initial Song file.
            while(myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if(data.split(" ").length!=1){
                    String line[]=data.split(" ");
                    int sid=Integer.parseInt(line[0]);
                    String name=line[1];
                    int time=Integer.parseInt(line[2]);
                    int heart=Integer.parseInt(line[3]);
                    int road=Integer.parseInt(line[4]);
                    int bliss=Integer.parseInt(line[5]);
                    Song song=new Song(sid,name,time,heart,road,bliss);
                    allSongs.add(song);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);

        }
        try {
            //reading test case file.
            File myObj = new File(testCaseFile);
            Scanner myReader = new Scanner(myObj);
            int lineNum=1;
            int lim=0;
            int heartLim=0;
            int roadLim=0;
            int blessLim=0;
            int playListNum=0;
            EpicBlend epicBlend=new EpicBlend();
            while(myReader.hasNextLine()) {

                String line=myReader.nextLine();
                if(lineNum==1){
                    //determine the limits from first line of test case file.
                    String[] data=line.split(" ");
                    lim=Integer.parseInt(data[0]);
                    heartLim=Integer.parseInt(data[1]);
                    roadLim=Integer.parseInt(data[2]);
                    blessLim=Integer.parseInt(data[3]);
                    lineNum++;
                }
                else if(lineNum==2){
                    //determine the playlist number from second line
                    String[] data=line.split(" ");
                    playListNum=Integer.parseInt(data[0]);
                    lineNum++;
                }
                else if(lineNum<=2+2*playListNum){
                    //determine the playlists songs and then adding this playlist to an array named allPlaylists to create epic blend after
                    String[] data=line.split(" ");
                    lineNum++;
                    String line2=myReader.nextLine();
                    lineNum++;
                    String[] data2=line2.split(" ");
                    PlayList pl=new PlayList(Integer.parseInt(data[0]));
                    for (int i=0; i<Integer.parseInt(data[1]);i++){
                        pl.addToPlayList(getSong(Integer.parseInt(data2[i])));
                    }
                    allPlayLists.add(pl);
                }
                else if(lineNum==3+2*playListNum){
                    //epic blend is created first time in the program
                    epicBlend.buildEpic(allPlayLists,lim,heartLim,roadLim,blessLim);
                    epicBlend.arrangeEpic();
                    lineNum++;
                }
                else{
                    if(line.contains("ADD")){
                        //if line contains "ADD" change the playlist and create a new epic Blend
                        String[] data=line.split(" ");
                        int plId=Integer.parseInt(data[2]);
                        int sid=Integer.parseInt(data[1]);
                        PlayList addedPl=getPl(plId);
                        Song addedSong=getSong(sid);
                        addedPl.addToPlayList(addedSong);
                        EpicBlend epicBlend1=new EpicBlend();
                        epicBlend1.buildEpic(allPlayLists,lim,heartLim,roadLim,blessLim);
                        epicBlend1.arrangeEpic();
                        findDifference(writer,epicBlend,epicBlend1);
                        epicBlend=epicBlend1;//keeping this blend in storage to compare it after
                    }
                    else if(line.contains("ASK")){
                        //if line contains "ASK" write the visible epic blend which sorted according to play count
                        writer.write(""+epicBlend.visibleEpic.get(0).sid);
                        for(int i=1;i<epicBlend.visibleEpic.size();i++){
                            writer.write(" "+epicBlend.visibleEpic.get(i).sid);
                        }
                        writer.write("\n");
                    }
                    else if(line.contains("REM")){
                        //if line contains "REM" arrange the playlists and create a new epic blend.
                        String[] data=line.split(" ");
                        int plId=Integer.parseInt(data[2]);
                        int sid=Integer.parseInt(data[1]);
                        PlayList removedPl=getPl(plId);
                        Song removedSong=getSong(sid);
                        removedPl.removePlayList(removedSong);
                        EpicBlend epicBlend1=new EpicBlend();
                        epicBlend1.buildEpic(allPlayLists,lim,heartLim,roadLim,blessLim);
                        epicBlend1.arrangeEpic();
                        findDifference(writer,epicBlend,epicBlend1);
                        epicBlend=epicBlend1;//keeping this blend in storage to compare it after
                    }
                    lineNum++;
                }
            }
            writer.close();
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    //method to find a song's id in allSongs arraylist.
    public static Song getSong(int sid){
        Song rSong = null;
        for (Song song : allSongs) {
            if (song.sid == sid) {
                rSong=song;
                break;
            }
        }
        return rSong;
    }
    //method to find Playlist from Playlist id. This method is used in add and remove commands.
    public static PlayList getPl(int pid){
        PlayList rPlayList = null;
        for (PlayList playList : allPlayLists) {
            if (playList.pid == pid) {
                rPlayList=playList;
                break;
            }
        }
        return rPlayList;
    }
    //find difference method is a long method but it is very simple. It takes two epic blends. One is old and the other one is new one.
    // It compares these two epic blends heartache, road, and blissfull categories and it finds the different songs and write it according the rules
    public static void findDifference(FileWriter writer, EpicBlend epicBlend, EpicBlend epicBlend1) throws IOException {
        ArrayList<Song> visibleHeart= epicBlend.visibleHeart;
        ArrayList<Song> visibleHeart1= epicBlend1.visibleHeart;
        Song heartAdd=null;
        Song heartRemove=null;
        for(Song visible1:visibleHeart1){

            if(!visibleHeart.contains(visible1)){
                heartAdd=visible1;
            }
        }
        for(Song visible:visibleHeart){

            if(!visibleHeart1.contains(visible)){
                heartRemove=visible;
            }
        }
        ArrayList<Song> visibleRoad= epicBlend.visibleRoad;
        ArrayList<Song> visibleRoad1= epicBlend1.visibleRoad;
        Song roadAdd=null;
        Song roadRemove=null;
        for(Song visible1:visibleRoad1){

            if(!visibleRoad.contains(visible1)){
                roadAdd=visible1;
            }
        }
        for(Song visible:visibleRoad){

            if(!visibleRoad1.contains(visible)){
                roadRemove=visible;
            }
        }
        ArrayList<Song> visibleBliss= epicBlend.visibleBliss;
        ArrayList<Song> visibleBliss1= epicBlend1.visibleBliss;
        Song blissAdd=null;
        Song blissRemove=null;
        for(Song visible1:visibleBliss1){

            if(!visibleBliss.contains(visible1)){
                blissAdd=visible1;
            }
        }

        for(Song visible:visibleBliss){

            if(!visibleBliss1.contains(visible)){
                blissRemove=visible;
            }
        }
        int s1;
        int s11;
        int s2;
        int s12;
        int s3;
        int s13;
        if(heartAdd!=null){
            s1=heartAdd.sid;
        }
        else{
            s1=0;
        }
        if(heartRemove!=null){
            s11=heartRemove.sid;
        }
        else{
            s11=0;
        }
        if(roadAdd!=null){
            s2=roadAdd.sid;
        }
        else{
            s2=0;
        }
        if(roadRemove!=null){
            s12=roadRemove.sid;
        }
        else{
            s12=0;
        }
        if(blissAdd!=null){
            s3=blissAdd.sid;
        }
        else{
            s3=0;
        }
        if(blissRemove!=null){
            s13=blissRemove.sid;
        }
        else{
            s13=0;
        }
        writer.write(s1+" "+s2+" "+s3+"\n");
        writer.write(s11+" "+s12+" "+s13+"\n");
    }
}