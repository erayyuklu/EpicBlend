import java.util.ArrayList;
@SuppressWarnings("unchecked")

public class EpicBlend {
    ArrayList<Song> blend=new ArrayList<>();
    int lim;
    int heartLim;
    int roadLim;
    int blissLim;
    ArrayList<Song> visibleEpic=new ArrayList<>();
    ArrayList<Song> visibleHeart=new ArrayList<>();
    ArrayList<Song> visibleRoad=new ArrayList<>();
    ArrayList<Song> visibleBliss=new ArrayList<>();

    ArrayList<PlayList> allPlayLists=new ArrayList<>();
    ArrayList<Song> allEpicSongs=new ArrayList<>();
    ArrayList<Song> heartMaxEpicSongs=new ArrayList<>();
    ArrayList<Song> roadMaxEpicSongs=new ArrayList<>();
    ArrayList<Song> blissMaxEpicSongs=new ArrayList<>();



    //this build epic method is constructor of Epic Blend Class.
    public void buildEpic(ArrayList<PlayList> allPlayLists,int lim, int heartLim, int roadLim, int blissLim){
        this.lim=lim;
        this.heartLim=heartLim;
        this.roadLim=roadLim;
        this.blissLim=blissLim;
        this.allPlayLists=allPlayLists;
        this.allEpicSongs=importAllinOne(allPlayLists);
        this.heartMaxEpicSongs= (ArrayList<Song>) quickSortByScore(allEpicSongs,0,allEpicSongs.size()-1,"heart").clone();
        this.roadMaxEpicSongs=(ArrayList<Song>)quickSortByScore(allEpicSongs,0,allEpicSongs.size()-1,"road").clone();
        this.blissMaxEpicSongs=(ArrayList<Song>)quickSortByScore(allEpicSongs,0,allEpicSongs.size()-1,"bliss").clone();
        //Cloning the sorted arrays to prevent the cause a change in sorted lists according to changes in cloned
    }

    //this method is the basis algorithm for this code. It controls for each song group. If a groups current size is smaller than it can be,
    // it controls playlists and their convenience to limits. If all these conditions are available, then it adds songs to this category from playlists
    public void arrangeEpic(){
        if(visibleHeart.size()<heartLim){
            for (Song heartMaxEpicSong : heartMaxEpicSongs) {
                if (compareHeart(allPlayLists.get(plIndex(heartMaxEpicSong))).size() < lim) {
                    visibleHeart.add(heartMaxEpicSong);
                    if(!visibleEpic.contains(heartMaxEpicSong)){
                        visibleEpic.add(heartMaxEpicSong);
                    }
                    if (visibleHeart.size() == heartLim) {
                        break;
                    }
                }
            }
        }
        if(visibleRoad.size()<roadLim){
            for (Song roadMaxEpicSong : roadMaxEpicSongs) {
                if (compareRoad(allPlayLists.get(plIndex(roadMaxEpicSong))).size() < lim) {
                    visibleRoad.add(roadMaxEpicSong);
                    if(!visibleEpic.contains(roadMaxEpicSong)){
                        visibleEpic.add(roadMaxEpicSong);
                    }
                    if (visibleRoad.size() == roadLim) {
                        break;
                    }
                }
            }
        }
        if(visibleBliss.size()<blissLim){
            for (Song blissMaxEpicSong : blissMaxEpicSongs) {
                if (compareBliss(allPlayLists.get(plIndex(blissMaxEpicSong))).size() < lim) {
                    visibleBliss.add(blissMaxEpicSong);
                    if(!visibleEpic.contains(blissMaxEpicSong)){
                        visibleEpic.add(blissMaxEpicSong);
                    }
                    if (visibleBliss.size() == blissLim) {
                        break;
                    }
                }
            }
        }
        quickSortByScore(visibleEpic,0,visibleEpic.size()-1,"time");//play count sorted visible Epic Blend
    }

    //It compares the visible epic Blend from heartache category and a playlist and returns the arraylist of identical songs which exists in each of them.
    private ArrayList<Song> compareHeart(PlayList playList){
        ArrayList<Song> compare=new ArrayList<>();
        for (int i=0;i<playList.playlistsongs.size();i++){
            for (Song song : visibleHeart) {
                if (playList.playlistsongs.get(i).equals(song)) {
                    compare.add(song);
                }
            }
        }
        return compare;
    }
    //It compares the visible epic Blend from road category and a playlist and returns the arraylist of identical songs which exists in each of them.
    private ArrayList<Song> compareRoad(PlayList playList){
        ArrayList<Song> compare=new ArrayList<>();
        for (int i=0;i<playList.playlistsongs.size();i++){
            for (Song song : visibleRoad) {
                if (playList.playlistsongs.get(i).equals(song)) {
                    compare.add(song);
                }
            }
        }
        return compare;
    }
    //It compares the visible epic Blend from blissfull category and a playlist and returns the arraylist of identical songs which exists in each of them.
    private ArrayList<Song> compareBliss(PlayList playList){
        ArrayList<Song> compare=new ArrayList<>();
        for (int i=0;i<playList.playlistsongs.size();i++){
            for (Song song : visibleBliss) {
                if (playList.playlistsongs.get(i).equals(song)) {
                    compare.add(song);
                }
            }
        }
        return compare;
    }
    //method that returns a given playlist's id.
    private int plIndex(Song song){
        int num=999;
        for (int i=0;i< allPlayLists.size();i++){
            for(int j=0; j<allPlayLists.get(i).playlistsongs.size();j++){
                if (song.equals(allPlayLists.get(i).playlistsongs.get(j))){
                    num=i;
                }
            }
        }
        return num;
    }

    //importing all playlists all songs into one Arraylist named allinOne and return them
    private ArrayList<Song> importAllinOne(ArrayList<PlayList> allPlayList){
        ArrayList<Song> allinOne =new ArrayList<>();
        for(int i=0;i<allPlayList.size();i++){
            for(int j=0;j<allPlayList.get(i).playlistsongs.size();j++){
                allinOne.add(allPlayList.get(i).playlistsongs.get(j));
            }
        }
        return allinOne;
    }

    //I use quick Sort algorithm in my code.
    public static ArrayList<Song> quickSortByScore(ArrayList<Song> songs, int low, int high, String scoreType) {


        if (low < high) {
            int pi = partitionByScore(songs, low, high, scoreType);

            quickSortByScore(songs, low, pi - 1, scoreType);
            quickSortByScore(songs, pi + 1, high, scoreType);
        }
        return songs;
    }
    //partititon method
    private static int partitionByScore(ArrayList<Song> songs, int low, int high, String scoreType) {

        int pivotScore = getScore(songs.get(high), scoreType);
        String pivotName = songs.get(high).name;
        int i = low - 1;

        for (int j = low; j < high; j++) {
            int currentScore = getScore(songs.get(j), scoreType);
            String currentName = songs.get(j).name;
            //ordering the songs according to determined score, if two elements score is same, then it order them lexicographically.
            if (currentScore > pivotScore || (currentScore == pivotScore && currentName.compareTo(pivotName) <= 0)) {
                i++;
                Song temp = songs.get(i);
                songs.set(i, songs.get(j));
                songs.set(j, temp);
            }
        }

        Song temp = songs.get(i + 1);
        songs.set(i + 1, songs.get(high));
        songs.set(high, temp);

        return i + 1;
    }
    //It is useful method to avoid different sort methods for different score types
    private static int getScore(Song song, String scoreType) {
        return switch (scoreType) {
            case "time" -> song.time;
            case "heart" -> song.heart;
            case "road" -> song.road;
            case "bliss" -> song.bliss;
            default -> 0; // Default score if scoreType doesn't match
        };
    }
}
