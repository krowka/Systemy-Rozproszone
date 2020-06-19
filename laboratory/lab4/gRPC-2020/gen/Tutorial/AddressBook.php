<?php
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: person.proto

namespace Tutorial;

use Google\Protobuf\Internal\GPBType;
use Google\Protobuf\Internal\RepeatedField;
use Google\Protobuf\Internal\GPBUtil;

/**
 * Generated from protobuf message <code>tutorial.AddressBook</code>
 */
class AddressBook extends \Google\Protobuf\Internal\Message
{
    /**
     * Generated from protobuf field <code>repeated .tutorial.Person people = 1;</code>
     */
    private $people;

    /**
     * Constructor.
     *
     * @param array $data {
     *     Optional. Data for populating the Message object.
     *
     *     @type \Tutorial\Person[]|\Google\Protobuf\Internal\RepeatedField $people
     * }
     */
    public function __construct($data = NULL) {
        \GPBMetadata\Person::initOnce();
        parent::__construct($data);
    }

    /**
     * Generated from protobuf field <code>repeated .tutorial.Person people = 1;</code>
     * @return \Google\Protobuf\Internal\RepeatedField
     */
    public function getPeople()
    {
        return $this->people;
    }

    /**
     * Generated from protobuf field <code>repeated .tutorial.Person people = 1;</code>
     * @param \Tutorial\Person[]|\Google\Protobuf\Internal\RepeatedField $var
     * @return $this
     */
    public function setPeople($var)
    {
        $arr = GPBUtil::checkRepeatedField($var, \Google\Protobuf\Internal\GPBType::MESSAGE, \Tutorial\Person::class);
        $this->people = $arr;

        return $this;
    }

}
