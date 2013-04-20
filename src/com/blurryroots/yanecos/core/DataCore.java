/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blurryroots.yanecos.core;

import gnu.trove.list.TLongList;
import gnu.trove.list.array.TLongArrayList;
import gnu.trove.list.linked.TLongLinkedList;
import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.TMap;
import gnu.trove.map.TObjectLongMap;
import gnu.trove.map.hash.THashMap;
import gnu.trove.map.hash.TLongObjectHashMap;
import gnu.trove.map.hash.TObjectLongHashMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO: make serializeable
/**
 * Houses all the data related to entities, data and processors
 *
 * @author Sven Freiberg
 */
public 
class DataCore {
    /*entity tables*/
    /**
     * Keeps track of the amount of entities which has been built.
     */
    private Long _entityIdCounter;
    //
    private long _entityNumberCounter;
    //
    private TLongObjectMap<Boolean> _entityId_setStatus_Table;
    private TMap<Boolean, TLongList> _setStatus_entityIdList_Table;
    /**
     * Relational-Map of an entity(id) -> its components.
     */
    private TLongObjectMap<THashMap<Class<? extends Data>, Data>> _entityId_Data_Table;
    /**
     * Relational-Map of a type of component -> its possessors.
     */
    private TMap<Class<? extends Data>, TLongList> _dataClasstype_entityId_Table;
    //
    /**
     * Relational-Map of an entity(id) -> its name.
     */
    private TLongObjectMap<String> _entityId_entityName_Table;
    /**
     * Relational-Map of an entity name -> its entity(id).
     */
    private TObjectLongMap<String> _entityName_entityId_Table;
    //
    /*entity tables*/
    
    
    /* processor tables */
    //
    /**
     * id counter for processors
     */
    private Long _processorIdCounter;
    /**
     * processor id -> processor object
     */
    private TLongObjectMap<DataProcessor> _processorId_ProcessorInstance_Table;
    /**
     * processor id -> processor class type
     */
    private TLongObjectMap<Class<? extends DataProcessor>> _processorId_processorClassType_Table;
    /**
     * classtype of processor -> porcessor id
     */
    private TObjectLongMap<Class<? extends DataProcessor>> _processorClassType_processorId_Table;
    /**
     * a list of processor ids
     */
    private TLongObjectMap<TLongList> _processorId_interestedEntity_Table;
    /**
     * a list of datatypes -> a list of interested processors ids
     */
    private Map<List<Class<? extends Data>>, TLongList> _data_InterestedProcessor_Table;
    //
    /* processor tables */
    
    
    //
    public
    DataCore() {
        //
    }

    //
    public
    void initialize() {
        //
        _entityIdCounter = 1L;
        _entityNumberCounter = 0L;
        //
        _entityId_setStatus_Table = new TLongObjectHashMap<Boolean>();
        _setStatus_entityIdList_Table = new THashMap<Boolean, TLongList>();
        _setStatus_entityIdList_Table.put( Boolean.TRUE, new TLongArrayList() );
        _setStatus_entityIdList_Table.put( Boolean.FALSE, new TLongLinkedList() );
        _setStatus_entityIdList_Table.get( Boolean.FALSE ).add( 1L );

        //
        _entityId_Data_Table = new TLongObjectHashMap<THashMap<Class<? extends Data>, Data>>( 128, 0.9f );
        _dataClasstype_entityId_Table = new THashMap<Class<? extends Data>, TLongList>();
        //
        _entityId_entityName_Table = new TLongObjectHashMap<String>( 128, 0.9f );
        _entityName_entityId_Table = new TObjectLongHashMap<String>( 128, 0.9f );

        //
        _processorIdCounter = 1L;
        _processorId_ProcessorInstance_Table = new TLongObjectHashMap<DataProcessor>( 8, 0.9f );
        //
        _processorId_processorClassType_Table = new TLongObjectHashMap<Class<? extends DataProcessor>>( 8, 0.8f );
        _processorClassType_processorId_Table = new TObjectLongHashMap<Class<? extends DataProcessor>>( 8, 0.8f );
        //
        _processorId_interestedEntity_Table = new TLongObjectHashMap<TLongList>();
        _data_InterestedProcessor_Table = new HashMap<List<Class<? extends Data>>, TLongList>();
    }

    /**
     * @return the _idCounter
     */
    public 
    Long getEntityIdCounter() {
        //
        return _entityIdCounter;
    }

    /**
     * @return the _entityCounter
     */
    public 
    long getEntityCounter() {
        //
        return _entityNumberCounter;
    }

    /**
     * @return the _entityId_setStatus_Table
     */
    public 
    TLongObjectMap<Boolean> getEntityId_setStatus_Table() {
        //
        return _entityId_setStatus_Table;
    }

    /**
     * @return the _setStatus_entityId_Table
     */
    public 
    TMap<Boolean, TLongList> getSetStatus_entityId_Table() {
        //
        return _setStatus_entityIdList_Table;
    }

    /**
     * @return the _entityComponentTable
     */
    public 
    TLongObjectMap<THashMap<Class<? extends Data>, Data>> getEntity_Data_Table() {
        //
        return _entityId_Data_Table;
    }

    /**
     * @return the _componentEntityTable
     */
    public 
    TMap<Class<? extends Data>, TLongList> getData_Entity_Table() {
        //
        return _dataClasstype_entityId_Table;
    }

    /**
     * @return the _entityNameTable
     */
    public 
    TLongObjectMap<String> getEntityNameTable() {
        //
        return _entityId_entityName_Table;
    }

    /**
     * @return the _nameEntityTable
     */
    public 
    TObjectLongMap<String> getNameEntityTable() {
        //
        return _entityName_entityId_Table;
    }

    /**
     * @return the _systemIdCounter
     */
    public 
    Long getDataProcessorIdCounter() {
        //
        return _processorIdCounter;
    }

    /**
     * @return the _id_System_Table
     */
    public 
    TLongObjectMap<DataProcessor> getId_Processor_Table() {
        //
        return _processorId_ProcessorInstance_Table;
    }

    /**
     * @return the _id_ClassType_Table
     */
    public 
    TLongObjectMap<Class<? extends DataProcessor>> getId_ClassType_Table() {
        //
        return _processorId_processorClassType_Table;
    }

    /**
     * @return the _classType_Id_Table
     */
    public 
    TObjectLongMap<Class<? extends DataProcessor>> getClassType_Id_Table() {
        //
        return _processorClassType_processorId_Table;
    }

    /**
     * @param idCounter the _idCounter to set
     */
    public 
    void setEntityIdCounter( Long idCounter ) {
        //
        this._entityIdCounter = idCounter;
    }

    /**
     * @param entityCounter the _entityCounter to set
     */
    public 
    void setEntityCounter( long entityCounter ) {
        //
        this._entityNumberCounter = entityCounter;
    }

    /**
     * @param systemIdCounter the _systemIdCounter to set
     */
    public 
    void setDataProcessorIdCounter( Long systemIdCounter ) {
        //
        this._processorIdCounter = systemIdCounter;
    }

    /**
     * @return the _systemId_interestedEntity_Table
     */
    public 
    TLongObjectMap<TLongList> getProcessId_interestedEntity_Table() {
        //
        return _processorId_interestedEntity_Table;
    }

    /**
     * @return list 
     */
    public 
    Map<List<Class<? extends Data>>, TLongList> getData_InterestedProcessor_Table() {
        //
        return _data_InterestedProcessor_Table;
    }
}
