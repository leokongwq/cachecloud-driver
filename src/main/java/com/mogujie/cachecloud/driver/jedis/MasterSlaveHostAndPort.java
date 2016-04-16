package com.mogujie.cachecloud.driver.jedis;

import redis.clients.jedis.HostAndPort;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: jiexiu
 * Date: 16/4/15
 * Time: 下午4:42
 * Email:jiexiu@mogujie.com
 */
public class MasterSlaveHostAndPort {

    private final String masterName;

    private final HostAndPort master;

    private final Set<HostAndPort> slaves;

    public MasterSlaveHostAndPort(String masterName, HostAndPort master, Set<HostAndPort> slaves) {
        super();
        this.masterName = masterName;
        this.master = master;
        this.slaves = slaves;
    }

    public String getMasterName() {
        return masterName;
    }

    public HostAndPort getMaster() {
        return master;
    }

    public Set<HostAndPort> getSlaves() {
        return slaves;
    }


    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((master == null) ? 0 : master.hashCode());
        result = prime * result
                + ((masterName == null) ? 0 : masterName.hashCode());
        result = prime * result
                + ((slaves == null) ? 0 : slaves.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MasterSlaveHostAndPort other = (MasterSlaveHostAndPort) obj;
        if (master == null) {
            if (other.master != null)
                return false;
        } else if (!master.equals(other.master))
            return false;
        if (masterName == null) {
            if (other.masterName != null)
                return false;
        } else if (!masterName.equals(other.masterName))
            return false;
        if (slaves == null) {
            if (other.slaves != null)
                return false;
        } else if (!slaves.equals(other.slaves))
            return false;
        return true;
    }

    public String toString() {
        return "{masterName=" + masterName + ", master=" + master + ", slaves="
                + slaves + "}";
    }
}
