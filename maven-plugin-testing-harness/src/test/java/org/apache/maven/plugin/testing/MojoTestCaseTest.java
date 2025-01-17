package org.apache.maven.plugin.testing;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;

import java.io.StringReader;
import java.util.Map;

/**
 * @author Jason van Zyl
 */
public class MojoTestCaseTest
    extends AbstractMojoTestCase
{

    private PlexusConfiguration pluginConfiguration;

    /** {@inheritDoc} */
    @Override
    protected void setUp()
        throws Exception
    {
        super.setUp();

        String pom = "<project>" +
                "<build>" +
                "<plugins>" +
                "<plugin>" +
                "<artifactId>maven-simple-plugin</artifactId>" +
                "<configuration>" +
                "<keyOne>valueOne</keyOne>" +
                "<keyTwo>valueTwo</keyTwo>" +
                "</configuration>" +
                "</plugin>" +
                "</plugins>" +
                "</build>" +
                "</project>";

        Xpp3Dom pomDom = Xpp3DomBuilder.build( new StringReader( pom ) );

        pluginConfiguration = extractPluginConfiguration( "maven-simple-plugin", pomDom );
    }

    /**
     */
    public void testPluginConfigurationExtraction()
    {
        assertEquals( "valueOne", pluginConfiguration.getChild( "keyOne" ).getValue() );

        assertEquals( "valueTwo", pluginConfiguration.getChild( "keyTwo" ).getValue() );
    }

    /**
     * @throws Exception if any
     */
    public void testMojoConfiguration()
        throws Exception
    {
        SimpleMojo mojo = new SimpleMojo();

        mojo = (SimpleMojo) configureMojo( mojo, pluginConfiguration );

        assertEquals( "valueOne", mojo.getKeyOne() );

        assertEquals( "valueTwo", mojo.getKeyTwo() );
    }

    /**
     * @throws Exception if any
     */
    public void testVariableAccessWithoutGetter()
        throws Exception
    {
        SimpleMojo mojo = new SimpleMojo();

        mojo = (SimpleMojo) configureMojo( mojo, pluginConfiguration );

        assertEquals( "valueOne", (String)getVariableValueFromObject( mojo, "keyOne" ) );

        assertEquals( "valueTwo", (String)getVariableValueFromObject( mojo, "keyTwo" ) );
    }

    /**
     * @throws Exception if any
     */
     public void testVariableAccessWithoutGetter2()
        throws Exception
    {
        SimpleMojo mojo = new SimpleMojo();

        mojo = (SimpleMojo) configureMojo( mojo, pluginConfiguration );

        Map<String, Object> map = getVariablesAndValuesFromObject( mojo );

        assertEquals( "valueOne", (String)map.get( "keyOne" ) );

        assertEquals( "valueTwo", (String)map.get( "keyTwo" ) );
    }

    /**
     * @throws Exception if any
     */
    public void testSettingMojoVariables()
        throws Exception
    {
        SimpleMojo mojo = new SimpleMojo();

        mojo = (SimpleMojo) configureMojo( mojo, pluginConfiguration );

        setVariableValueToObject( mojo, "keyOne", "myValueOne" );

        assertEquals( "myValueOne", (String)getVariableValueFromObject( mojo, "keyOne" ) );

    }

}
