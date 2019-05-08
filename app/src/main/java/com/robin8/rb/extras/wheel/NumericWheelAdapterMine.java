package com.robin8.rb.extras.wheel;

import java.util.List;

/**
 * Numeric Wheel adapter.
 */
public class NumericWheelAdapterMine implements WheelAdapter {
	List<String> list;
	public NumericWheelAdapterMine(List<String> list){
        this.list = list;
    }

	@Override
	public int getItemsCount() {
		return list.size();
	}

	@Override
	public Object getItem(int index) {
		return list.get(index);
	}

	@Override
	public int indexOf(Object o) {
        for(int i=0;i<list.size();i++){
            if(o.toString().equals(list.get(i))){
                return i;
            }
        }
        return 0;
	}
}
