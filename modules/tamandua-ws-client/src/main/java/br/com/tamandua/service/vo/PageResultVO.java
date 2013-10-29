/**
 * 
 */
package br.com.tamandua.service.vo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Everton Rosario
 */
@XmlRootElement(name="page-result")
public class PageResultVO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private int start;
    private int pageSize;
    private String sortField;
    private boolean ascending;
    private int totalPages;
    private int totalResults;
    private boolean hasMoreResults;
    private List<ResultItemVO> resultList = new ArrayList<ResultItemVO>(50);
    
    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public boolean isHasMoreResults() {
        return hasMoreResults;
    }

    public void setHasMoreResults(boolean hasMoreResults) {
        this.hasMoreResults = hasMoreResults;
    }


    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public List<ResultItemVO> getResultList() {
        return resultList;
    }

    public void setResultList(List<ResultItemVO> resultList) {
        this.resultList = resultList;
    }

    public ResultItemVO getFirstItem() {
        if (resultList != null && !resultList.isEmpty()) {
            return resultList.get(0);
        }

        return null;
    }
}
