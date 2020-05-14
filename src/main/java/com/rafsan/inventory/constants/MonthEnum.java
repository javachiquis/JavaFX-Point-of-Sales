package com.rafsan.inventory.constants;

public enum MonthEnum {

    January(1, "enero"),
    February(2, "febrero"),
    March(3, "marzo"),
    April(4, "abril"),
    May(5, "mayo"),
    June(6, "junio"),
    July(7, "julio"),
    August(8, "agosto"),
    September(9, "septiembre"),
    October(10, "octubre"),
    November(11, "noviembre"),
    December(12, "diciembre");

    private final int index;
    private final String name;

    MonthEnum(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public static int getIndexByName(String name) {
        for (MonthEnum monthEnum : MonthEnum.values()) {
            if(monthEnum.getName().equalsIgnoreCase(name)) {
                return monthEnum.getIndex();
            }
        }

        return 0;
    }
}
