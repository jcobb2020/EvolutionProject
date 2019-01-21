package World;

public enum MoveDirection {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NOTRHWEST,
    WRONG;

    public static MoveDirection parseFromInt(int i){
        switch (i){
            case 0:
                return NORTH;

            case 1:
                return NORTHEAST;

            case 2:
                return EAST;
            case 3:
                return SOUTHEAST;
            case 4:
                return SOUTH;

            case 5:
                return SOUTHWEST;

            case 6:
                return WEST;
            case 7:
                return NOTRHWEST;
            default: return WRONG;
        }
    }
}
