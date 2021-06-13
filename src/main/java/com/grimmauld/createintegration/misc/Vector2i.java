package com.grimmauld.createintegration.misc;

import net.minecraft.util.math.BlockPos;

public class Vector2i {
    public int x; // Exhibit A, lol: //nicht x=0 da intellij mich sonst nerft
    public int y;
    Vector2i(int x,int y){
        this.x=x;
        this.y=y;
    }
    Vector2i(BlockPos p){ this(p.getX(),p.getZ());}

    Vector2i(long l){//Todo(Test if this really works)
        this((int)(l>>32),(int)l);
    }

    public int hashCode() { return (this.y + this.x * 31); }
    public boolean equals(Object o){
        if(!(o instanceof Vector2i))return false;
        else {
            Vector2i other=(Vector2i)o;
            return other.x==x && other.y==y;
        }
    }

    public int compareTo(Vector2i o) {
        if(x!=o.x)return x-o.x;
        else if(y!=o.y)return y-o.y;
        return 0;
    }
    public long toLong(){//Todo(Test if this really works)
        return ((long)x)<<32 | (y & 0xffffffffL);
    }

    @Override
    public String toString() {
        return "("+x+"|"+y+")";
    }

    public Vector2i div(int n){return new Vector2i(x/n,y/n);}
    public Vector2i times(int n){return new Vector2i(x*n,y*n);}
    public Vector2i plus(Vector2i n){return new Vector2i(x+n.x,y+n.y);}
    public Vector2i sub(Vector2i n){return new Vector2i(x-n.x,y-n.y);}
}
