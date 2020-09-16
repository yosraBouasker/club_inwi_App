
package com.inwi.clubinwi.achoura.models.reserver;

import java.util.List;
import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class Filleuls {

    @Expose
    private Long count;
    @Expose
    private List<Object> list;

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<Object> getList() {
        return list;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }

}
