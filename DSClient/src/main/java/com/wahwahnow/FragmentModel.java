package com.wahwahnow;

public class FragmentModel {

    private int fragmentID;
    private long fragmentSize;

    public FragmentModel(){

    }

    public FragmentModel(int fragmentID, long fragmentSize){
        this.fragmentID = fragmentID;
        this.fragmentSize = fragmentSize;
    }

    public int getFragmentID() {
        return fragmentID;
    }

    public void setFragmentID(int fragmentID) {
        this.fragmentID = fragmentID;
    }

    public long getFragmentSize() {
        return fragmentSize;
    }

    public void setFragmentSize(long fragmentSize) {
        this.fragmentSize = fragmentSize;
    }

    public String toString(){
        return "FragmentID: "+fragmentID+" FragmentSize: "+fragmentSize;
    }

}
