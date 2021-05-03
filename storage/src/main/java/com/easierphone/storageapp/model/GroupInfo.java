package com.easierphone.storageapp.model;

import com.easierphone.storageapp.fragment.RootsFragment.Item;

import java.util.List;

/**
 * Created by Ayyaz on 07/08/16.
 */

public class GroupInfo {
    public String label;
    public List<Item> itemList;

    public GroupInfo(String text, List<Item> list){
        label = text;
        itemList = list;
    }
}
