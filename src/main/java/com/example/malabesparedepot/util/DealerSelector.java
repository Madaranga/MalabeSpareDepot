package com.example.malabesparedepot.util;

import com.example.malabesparedepot.model.Dealer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DealerSelector {
    public static List<Dealer> getRandomFoundDealers(List<Dealer> allDealers) {
        List<Dealer> selected = new ArrayList<>();

        //
        if (allDealers.size() <=4) {
            selected.addAll(allDealers);
            CustomSorter.sortDealersByLocation(selected);
            return selected;
        }

        Random random = new Random();

        //
        while (selected.size() < 4) {
            int randomIndex = random.nextInt(allDealers.size());
            Dealer candidate = allDealers.get(randomIndex);

            //check duplicates
            boolean isDuplicate = false;
            for (Dealer d : selected) {
                if (d.getDealerId().equals(candidate.getDealerId())) {
                    isDuplicate = true;
                    break;
                }
            }

            if (!isDuplicate) {
                selected.add(candidate);
            }

        }

        //use custom algorithm that we created - sort by location
        CustomSorter.sortDealersByLocation(selected);
        return selected;
    }
}
