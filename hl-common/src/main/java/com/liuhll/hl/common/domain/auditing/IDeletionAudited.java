package com.liuhll.hl.common.domain.auditing;

import java.util.Date;


public interface IDeletionAudited extends  ISoftDelete  {
    Long getDeleteBy();
    void setDeleteBy(Long deleteBy);

    Date getDeleteTime();
    void setDeleteTime(Date deleteTime);
}
