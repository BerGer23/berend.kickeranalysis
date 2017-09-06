public class Player
{
    static final int SPIELTAG = 2;

    String _name;

    POS _pos;

    float _price;

    int _ptsThisSeason;

    int _ptsPrediction;

    int _ptsLastSeason;

    float _ptsPerMioLastSeason;

    float _ptsPredictionPerMio;

    Player( final String name, final String pos, final int ptsThisSeason, final float price, final int ptsLastSeason )
    {
        _name = name;
        _pos = POS.valueOf( pos.toUpperCase() );
        _ptsThisSeason = ptsThisSeason;
        _price = price;
        _ptsLastSeason = ptsLastSeason;
        _ptsPerMioLastSeason = ( float ) ( ptsLastSeason / Math.pow( price, 0.60 ) );
        _ptsPrediction = ptsThisSeason * 34 / SPIELTAG;
        _ptsPredictionPerMio = ( float ) ( _ptsPrediction / Math.pow( price, 0.60 ) );
    }

    public String toString()
    {
        return "Name='" + _name + '\'' +
                ", Position=" + _pos +
                ", Price=" + _price +
                ", PointsThisSeason=" + _ptsThisSeason +
                ", PointsLastSeason=" + _ptsLastSeason +
                ", PointsPerMillion=" + _ptsPerMioLastSeason +
                ", PointsPrediction=" + _ptsPrediction +
                ", PointsPredictionPerMillion=" + _ptsPredictionPerMio;
    }

    enum POS
    {
        TOR, ABW, MIT, STU
    }
}
