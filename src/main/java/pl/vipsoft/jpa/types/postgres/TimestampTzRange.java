package pl.vipsoft.jpa.types.postgres;

import java.util.Date;

/**
 * Created by zabian on 12.12.14.
 */
public class TimestampTzRange {

    private Date dateFrom;
    private Date dateTo;
    private boolean leftOpen;
    private boolean rightOpen;

    public TimestampTzRange() {
    }

    public TimestampTzRange(Date dateFrom, Date dateTo) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public TimestampTzRange(Date dateFrom, Date dateTo, boolean leftOpen, boolean rightOpen) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.leftOpen = leftOpen;
        this.rightOpen = rightOpen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimestampTzRange)) return false;

        TimestampTzRange that = (TimestampTzRange) o;

        if (leftOpen != that.leftOpen) return false;
        if (rightOpen != that.rightOpen) return false;
        if (dateFrom != null ? !dateFrom.equals(that.dateFrom) : that.dateFrom != null) return false;
        if (dateTo != null ? !dateTo.equals(that.dateTo) : that.dateTo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = dateFrom != null ? dateFrom.hashCode() : 0;
        result = 31 * result + (dateTo != null ? dateTo.hashCode() : 0);
        result = 31 * result + (leftOpen ? 1 : 0);
        result = 31 * result + (rightOpen ? 1 : 0);
        return result;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public boolean isLeftOpen() {
        return leftOpen;
    }

    public void setLeftOpen(boolean leftOpen) {
        this.leftOpen = leftOpen;
    }

    public boolean isRightOpen() {
        return rightOpen;
    }

    public void setRightOpen(boolean rightOpen) {
        this.rightOpen = rightOpen;
    }

    public boolean isInfinityFrom() {
        return dateFrom == null;
    }

    public void setInfinityFrom() {
        this.dateFrom = null;
    }

    public boolean isInfinityTo() {
        return dateTo == null;
    }

    public void setInfinityTo() {
        this.dateTo = null;
    }
}
