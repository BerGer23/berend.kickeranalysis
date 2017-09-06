import java.util.*;

public class TeamCreator {

    private static final float BUDGET = 42.5f;

    static void createTeam(final List<Player> tor, final List<Player> abwehr, final List<Player> mittelfeld, final List<Player> sturm, final int cTor, final int cAbw, final int cMf, final int cSt) {
        final float maxBudget = (float) (BUDGET - (0.2 * (22 - (cAbw + cMf + cSt + cTor))));
        int maxPts = 0;
        List<Player> bestTeam = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            final List<Player> allPlayers = new ArrayList<>();
            allPlayers.addAll(tor);
            allPlayers.addAll(abwehr);
            allPlayers.addAll(mittelfeld);
            allPlayers.addAll(sturm);
            allPlayers.sort(new Comparator<Player>() {
                @Override
                public int compare(final Player o1, final Player o2) {
                    return (int) (o2._ptsPredictionPerMio - o1._ptsPredictionPerMio);
                }
            });
            final HashMap<Player.POS, Integer> posiCount = new HashMap<>();
            posiCount.put(Player.POS.TOR, cTor);
            posiCount.put(Player.POS.ABW, cAbw);
            posiCount.put(Player.POS.MIT, cMf);
            posiCount.put(Player.POS.STU, cSt);
            double budget = 0;
            boolean teamIsFull = false;
            final List<Player> team = new ArrayList<>();
            do {
                for (final Player player : allPlayers) {
                    if (!team.contains(player)) {
                        if (posiCount.get(player._pos) > 0 && budget + player._price < maxBudget) {
                            team.add(player);
                            posiCount.put(player._pos, posiCount.get(player._pos) - 1);
                            budget += player._price;
                        } else if (posiCount.get(player._pos) > 0) {
                            final Player toRemove = team.remove((int) ((Math.random() * team.size())));
                            budget -= toRemove._price;
                            posiCount.put(toRemove._pos, posiCount.get(toRemove._pos) + 1);
                        }
                        teamIsFull = true;
                        for (final Integer count : posiCount.values()) {
                            if (count > 0) {
                                teamIsFull = false;
                                break;
                            }
                        }
                    }
                }
            }
            while (!teamIsFull);
            int ptsLastSeason = 0;
            for (final Player player : team) {
                ptsLastSeason += player._ptsPrediction;
            }
            if (maxPts < ptsLastSeason) {
                maxPts = ptsLastSeason;
                bestTeam = team;
            }
        }
        System.out.println("Whole team: " + maxPts);

        float gesamt = 0;
        for (final Player player : bestTeam) {
            System.out.println(player._name + " " + player._ptsPrediction);
            gesamt += player._price;
        }
        System.out.println("Costs: " + gesamt);
    }

    private static void refreshMaxima(final EnumMap<Player.POS, Integer> posiCount, final EnumMap<Player.POS, Integer> posiMaxCount) {
        if (posiCount.get(Player.POS.STU) == 3) {
            posiMaxCount.replace(Player.POS.MIT, 4);
        }
        if (posiCount.get(Player.POS.MIT) == 5 && posiCount.get(Player.POS.STU) == 2) {
            posiMaxCount.replace(Player.POS.ABW, 3);
            posiMaxCount.replace(Player.POS.STU, 2);
        } else if (posiCount.get(Player.POS.MIT) == 5 && posiCount.get(Player.POS.ABW) == 4) {
            posiMaxCount.replace(Player.POS.STU, 1);
        } else if (posiCount.get(Player.POS.MIT) == 5) {
            posiMaxCount.replace(Player.POS.STU, 2);
        }
    }

    static void createTeamStefan(final List<Player> tor, final List<Player> abwehr, final List<Player> mittelfeld, final List<Player> sturm) {
        double currentBudget = (float) (BUDGET - (0.2 * 22));

        int maxPts = 0;
        List<Player> bestTeam = new ArrayList<>();
//        for (int i = 0; i < 1000000; i++) {
        final List<Player> allPlayers = new ArrayList<>();
        allPlayers.addAll(tor);
        allPlayers.addAll(abwehr);
        allPlayers.addAll(mittelfeld);
        allPlayers.addAll(sturm);
        allPlayers.sort(new Comparator<Player>() {
            @Override
            public int compare(final Player o1, final Player o2) {
                return (int) (o2._ptsPredictionPerMio - o1._ptsPredictionPerMio);
            }
        });
        final EnumMap<Player.POS, Integer> posiCount = new EnumMap<>(Player.POS.class);
        posiCount.put(Player.POS.TOR, 0);
        posiCount.put(Player.POS.ABW, 0);
        posiCount.put(Player.POS.MIT, 0);
        posiCount.put(Player.POS.STU, 0);
        final EnumMap<Player.POS, Integer> posiMaxCount = new EnumMap<>(Player.POS.class);
        posiMaxCount.put(Player.POS.TOR, 1);
        posiMaxCount.put(Player.POS.ABW, 4);
        posiMaxCount.put(Player.POS.MIT, 5);
        posiMaxCount.put(Player.POS.STU, 3);
        double budget = 0;
        boolean teamIsFull = false;
        final List<Player> team = new ArrayList<>();
        do {
            for (final Player player : allPlayers) {
                if (!team.contains(player)) {
                    if (posiCount.get(player._pos) <= posiMaxCount.get(player._pos) && budget + player._price < currentBudget) {
                        team.add(player);
                        posiCount.put(player._pos, posiCount.get(player._pos) + 1);
                        budget += player._price;
                        currentBudget = currentBudget - budget - 0.2;
                        refreshMaxima(posiCount, posiMaxCount);
                    }
                    teamIsFull = true;
                    for (final Integer count : posiCount.values()) {
                        if (count > 0) {
                            teamIsFull = false;
                            break;
                        }
                    }
                }
            }
        }
        while (!teamIsFull);
        int ptsLastSeason = 0;
        for (final Player player : team) {
            ptsLastSeason += player._ptsPrediction;
        }
        if (maxPts < ptsLastSeason) {
            maxPts = ptsLastSeason;
            bestTeam = team;
        }
//        }
        System.out.println("Whole team: " + maxPts);

        float gesamt = 0;
        for (final Player player : bestTeam) {
            System.out.println(player._name + " " + player._ptsPrediction);
            gesamt += player._price;
        }
        System.out.println("Costs: " + gesamt);
    }
}
