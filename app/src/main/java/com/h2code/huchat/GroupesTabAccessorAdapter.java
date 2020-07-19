package com.h2code.huchat;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class GroupesTabAccessorAdapter extends FragmentPagerAdapter
{

    public GroupesTabAccessorAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int i)
    {
        switch (i)
        {
            case 0:
                GroupesChatsFragment GroupeschatsFragment = new GroupesChatsFragment();
                return GroupeschatsFragment;

            case 1:
                GroupsFragment GroupesgroupsFragment = new GroupsFragment();
                return GroupesgroupsFragment;

            case 2:
                GroupesContacsFragmnt GroupescontactsFragment = new GroupesContacsFragmnt();
                return GroupescontactsFragment;



            default:
                return null;
        }
    }


    @Override
    public int getCount()
    {
        return 3;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        switch (position)
        {
            case 0:
                return "Groupes \n Chats";

            case 1:
                return "Groupes \n Groups";

            case 2:
                return "Groupes \n Contacts";

          

            default:
                return null;
        }
    }
}
