package net.canopy.app.api;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Base filter interface for canopy filters.
 * Only use this when {@link IFilter.ILoadFilter} and {@link IFilter.IStoreFilter} are inapropriate.
 */
public interface IFilter {

    /**
     * Apply the filter to a given JSON tree.
     *
     * @param jsonNode JsonNode from the Jackson library representing an arbitrary JSON tree structure.
     *                 This is the output of the previous filter, or null if this is the first filter in the chain (only possible with {@link IFilter.ILoadFilter}).
     * @param argument Arbitrary argument string passed through directly from the user.
     * @return JsonNode representing the output JSON tree structure of this filter - may never be null.
     * @throws FilterException If an error occurs while applying the filter.
     *
     * @see IFilter.ILoadFilter
     * @see IFilter.IStoreFilter
     */
    JsonNode apply(JsonNode jsonNode, String argument) throws FilterException;





    /**
     * Canopy filter interface for filters that don't use the JSON tree structure that is passed to {@link IFilter#apply(JsonNode, String)}.
     * This should be used for filters that load or generate their own data.
     * Only filters that implement this interface can be the first filter in the chain.
     *
     * This interface does not add anything to {@link IFilter}, it is only used to ensure a sensible order of the filters in the filter chain.
     * Use in conjunction with {@link IFilter.IStoreFilter} is valid.
     *
     * @see IFilter#apply(JsonNode, String)
     * @see IFilter.IStoreFilter
     */
    static interface ILoadFilter extends IFilter {}





    /**
     * Canopy filter interface for filters that somehow store/export data from the JSON tree structure that is passed to {@link IFilter#apply(JsonNode, String)}.
     * Only filters that implement this interface can be the last filter in the chain, or the last filter before another {@link IFilter.ILoadFilter}.
     * Store filters must still return a non-null value from {@link #apply(JsonNode, String)}.
     * Simply returning the input value allows for nice chaining of multiple store-filters.
     *
     * This interface does not add anything to {@link IFilter}, it is only used to ensure a sensible order of the filters in the filter chain.
     * Use in conjunction with {@link ILoadFilter} is valid.
     *
     * @see IFilter#apply(JsonNode, String)
     * @see IFilter.ILoadFilter
     */
    static interface IStoreFilter extends IFilter {}

}
