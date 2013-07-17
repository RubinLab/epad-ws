package edu.stanford.isis.epadws.db.mysql.pipeline;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epadws.db.mysql.MySqlInstance;
import edu.stanford.isis.epadws.db.mysql.MySqlQueries;
import edu.stanford.isis.epadws.db.mysql.model.SeriesOrder;
import edu.stanford.isis.epadws.db.mysql.model.SeriesOrderStatus;

/**
 * Singleton class to keep track of all images and which are in the pipeline.
 *
 *
 * @author alansnyder
 */
public class SeriesOrderTracker
{

    @SuppressWarnings("unused") // Static initializer
	private static final ProxyLogger logger = ProxyLogger.getInstance();

    private static SeriesOrderTracker ourInstance = new SeriesOrderTracker();

    final Map<String,SeriesOrderStatus> statusMap = new ConcurrentHashMap<String, SeriesOrderStatus>();

    final Map<String,Float> completionMap = new ConcurrentHashMap<String, Float>();

    public static SeriesOrderTracker getInstance() {
        return ourInstance;
    }

    private SeriesOrderTracker() {
    }

    public void add(SeriesOrderStatus seriesOrderStatus){
        if(seriesOrderStatus==null){throw new IllegalArgumentException("seriesOrderStatus cannot be null.");}
        SeriesOrder so = seriesOrderStatus.getSeriesOrder();
        if(so==null){throw new IllegalArgumentException("SeriesOrder cannot be null.");}
        String seriesUID = so.getSeriesUID();
        statusMap.put(seriesUID,seriesOrderStatus);
    }

    public void remove(SeriesOrderStatus seriesOrderStatus){
        String seriesUID = seriesOrderStatus.getSeriesOrder().getSeriesUID();
        statusMap.remove(seriesUID);
    }

    public Set<SeriesOrderStatus> getStatusSet(){
        return new HashSet<SeriesOrderStatus>(statusMap.values());
    }

    public SeriesOrderStatus get(String seriesUID){
        return statusMap.get(seriesUID);
    }

    /**
     * Get a cached result for the percent complete, since in most cases it will be 100%.
     * If the series doesn't have the value calculated. (Like on the first call, then calculate
     * and cache the result.)
     * @param seriesUID String
     * @return float value between 0.0 and 100.0 percent.
     */
    public float getPercentComplete(String seriesUID){
        float percentComplete=-1.0f;
        if( !completionMap.containsKey(seriesUID) ){
            //call the database to calculate it now.
            MySqlQueries queries = MySqlInstance.getInstance().getMysqlQueries();
            percentComplete = queries.getPercentComplete(seriesUID);
            completionMap.put(seriesUID,percentComplete);
        }else{
            percentComplete = completionMap.get(seriesUID);
        }
        return percentComplete;
    }

    /**
     *
     * @param seriesUID String
     * @param percentComplete float
     */
    public void setPercentComplete(String seriesUID, float percentComplete){
        completionMap.put(seriesUID,percentComplete);
    }

}
