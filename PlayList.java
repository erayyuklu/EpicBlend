import java.util.ArrayList;

public class PlayList {
    int pid;
    ArrayList<Song> playlistsongs=new ArrayList<>();
    public PlayList(int pid){
        this.pid=pid;
    }
    //Method for adding new songs to playlist
    public void addToPlayList(Song song){
        playlistsongs.add(song);
    }
    //Method for removing songs from playlist
    public void removePlayList(Song song){
        playlistsongs.remove(song);
    }
}
